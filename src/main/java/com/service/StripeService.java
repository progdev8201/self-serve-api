package com.service;

import com.mapping.StripeSubscriptionProductToStripeSubscriptionProductDTO;
import com.mapping.SubscriptionEntityToSubscriptionEntityDTO;
import com.model.dto.*;
import com.model.dto.requests.*;
import com.model.entity.*;
import com.repository.*;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StripeService {
    @Value("${stripe.apiKey}")
    private String stripeAPIKey;

    @Value("${front-end.url}")
    String frontEndUrl;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private SubscriptionEntityRepository subscriptionEntityRepository;

    @Autowired
    private StripeSubscriptionProductRepository stripeSubscriptionProductRepository;

    private static String STRIPE_PAID_INVOICE = "paid";

    public StripeCreateAccountUrlDTO createStripeAccount(String ownerUsername) throws StripeException {
        Stripe.apiKey = stripeAPIKey;

        Owner owner = ownerRepository.findByUsername(ownerUsername).get();


        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setEmail(owner.getUsername())
                        .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                        .setBusinessProfile(AccountCreateParams.BusinessProfile.builder()
                                .setProductDescription("produitDescription")
                                .setName(owner.getRestaurantList().get(0).getName())
                                .setSupportEmail(owner.getUsername()).build())
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.CARD_PAYMENTS)
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.TRANSFERS)
                        .setCompany(AccountCreateParams.Company.builder()
                                .setName(owner.getRestaurantList().get(0).getName())
                                .build())
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .build();
        Account account = null;
        if (Objects.isNull(owner.getStripeAccountId())) {
            account = Account.create(params);
            owner.setStripeAccountId(account.getId());
            owner.setStripeEnable(false);
            ownerRepository.save(owner);
        } else {
            account = Account.retrieve(owner.getStripeAccountId());
        }

        AccountLinkCreateParams accountLinkparams =
                AccountLinkCreateParams.builder()
                        .setAccount(account.getId())
                        .setRefreshUrl(frontEndUrl + "/isStripeEnabled")
                        .setReturnUrl(frontEndUrl + "/isStripeEnabled?accountId=" + account.getId())
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();
        AccountLink accountLink = AccountLink.create(accountLinkparams);
        StripeCreateAccountUrlDTO stripeCreateAccountUrlDTO = new StripeCreateAccountUrlDTO();
        stripeCreateAccountUrlDTO.setValue(accountLink.getUrl());
        return stripeCreateAccountUrlDTO;
    }

    //TODO pk on dois faire ca?
    public void initApplePay() throws StripeException {
        Stripe.apiKey = stripeAPIKey;
        ApplePayDomainCreateParams params =
                ApplePayDomainCreateParams.builder()
                        .setDomainName(frontEndUrl)
                        .build();

        ApplePayDomain domain = ApplePayDomain.create(params);

    }

    public void saveStripeAccountId(String username) {
        Owner owner = ownerRepository.findByUsername(username).get();
        owner.setStripeEnable(true);
        ownerRepository.save(owner);
    }

    public StripeAccountIdDTO getAccountId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        Owner owner = restaurant.getOwner();
        StripeAccountIdDTO stripeAccountIdDTO = new StripeAccountIdDTO();
        if (Objects.nonNull(owner.getStripeEnable()) && owner.getStripeEnable()) {
            stripeAccountIdDTO.setValue(owner.getStripeAccountId());
        }
        return stripeAccountIdDTO;
    }

    public StripeClientSecretDTO processPayment(String restaurentStripeAccount, Bill bill) throws StripeException {
        Stripe.apiKey = stripeAPIKey;

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(Math.round(bill.getPrixTotal().doubleValue()) * 100)
                        .setCurrency("cad")
                        .setTransferData(PaymentIntentCreateParams.TransferData.builder().setDestination(restaurentStripeAccount).build())
                        .addPaymentMethodType("card")
                        .build();
        ArrayList paymentMethodTypes = new ArrayList();
        paymentMethodTypes.add("card");

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        StripeClientSecretDTO stripeClientSecretDTO = new StripeClientSecretDTO();
        stripeClientSecretDTO.setValue(paymentIntent.getClientSecret());
        return stripeClientSecretDTO;
    }

    public StripeClientSecretDTO processRequestPayment(String restaurentStripeAccount, Bill bill) throws StripeException {
        Stripe.apiKey = stripeAPIKey;

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setCurrency("cad")
                        .setAmount(Math.round(bill.getPrixTotal().doubleValue()) * 100)
                        .setTransferData(PaymentIntentCreateParams.TransferData.builder().setDestination(restaurentStripeAccount).build())
                        // Verify your integration in this guide by including this parameter
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        StripeClientSecretDTO stripeClientSecretDTO = new StripeClientSecretDTO();
        stripeClientSecretDTO.setValue(paymentIntent.getClientSecret());
        return stripeClientSecretDTO;
    }

    public StripeSessionCustomerIdDTO createCheckoutSession(String customerEmail) throws StripeException {
        Stripe.apiKey = stripeAPIKey;
        Owner owner = ownerRepository.findByUsername(customerEmail).get();

        CustomerCreateParams customerParams = CustomerCreateParams
                .builder()
                .setEmail(owner.getUsername())
                .build();
        // Create a new customer object
        if (Objects.nonNull(owner.getStripeCustomerId())) {
            return returnStripeSessionCustomerIdDTO(owner);
        }

        Customer customer = Customer.create(customerParams);
        owner.setStripeCustomerId(customer.getId());
        ownerRepository.save(owner);

        //we use StripeObject.PRETTY_PRINT_GSON.toJson() so that we get the JSON our client is expecting on the polymorphic
        //parameters that can either be object ids or the object themselves. If we tried to generate the JSON without call this,
        //for example, by calling gson.toJson(responseData) we will see something like "customer":{"id":"cus_XXX"} instead of
        //"customer":"cus_XXX".
        //If you only need to return 1 object, you can use the built in serializers, i.e. Subscription.retrieve("sub_XXX").toJson()
        return returnStripeSessionCustomerIdDTO(owner);

    }

    public List<StripeSubscriptionProductsDTO> fetchSubscriptionProducts() {
        return stripeSubscriptionProductRepository.findAll().stream().map(stripeSubscriptionProducts ->
                StripeSubscriptionProductToStripeSubscriptionProductDTO.instance.convert(stripeSubscriptionProducts))
                .collect(Collectors.toList());
    }

    public SubscriptionEntityDTO createSubscription(SubscriptionRequestDTO subscriptionRequestDTO) throws StripeException, JSONException {
        Customer customer = Customer.retrieve(subscriptionRequestDTO.getCustomerId());
        Owner owner = ownerRepository.findByStripeCustomerId(subscriptionRequestDTO.getCustomerId()).get();
        StripeSubscriptionProducts stripeSubscriptionProducts = stripeSubscriptionProductRepository.findByPriceId(subscriptionRequestDTO.getPriceId()).get();
        try {
            // Set the default payment method on the customer
            setPaymentMethod(subscriptionRequestDTO, customer);
        } catch (CardException e) {
            // Since it's a decline, CardException will be caught
            return setErrorInSubscriptionEntity(e);
        }

        updateCustomerParams(subscriptionRequestDTO, customer);

        // Create the subscription
        SubscriptionCreateParams subCreateParams = SubscriptionCreateParams
                .builder()
                .addItem(
                        SubscriptionCreateParams
                                .Item.builder()
                                .setPrice(subscriptionRequestDTO.getPriceId())
                                .build()
                )
                .setCustomer(customer.getId())
                .addAllExpand(Arrays.asList("latest_invoice.payment_intent"))
                .build();

        Subscription subscription = Subscription.create(subCreateParams);
        SubscriptionEntity subscriptionEntity = populateSubscriptionEntity(subscription, Optional.ofNullable(owner.getSubscriptionEntity()).orElse(new SubscriptionEntity()));
        subscriptionEntity.setStripeSubscriptionProducts(stripeSubscriptionProducts);
        subscriptionEntity.setOwner(owner);
        owner.setSubscriptionEntity(subscriptionEntity);
        ownerRepository.save(owner);
        return SubscriptionEntityToSubscriptionEntityDTO.instance.convert(subscriptionEntity);
    }

    private void updateCustomerParams(SubscriptionRequestDTO subscriptionRequestDTO, Customer customer) throws StripeException {
        CustomerUpdateParams customerUpdateParams = CustomerUpdateParams
                .builder()
                .setInvoiceSettings(
                        CustomerUpdateParams
                                .InvoiceSettings.builder()
                                .setDefaultPaymentMethod(subscriptionRequestDTO.getPaymentMethodId())
                                .build()
                )
                .build();

        customer.update(customerUpdateParams);
    }

    public SubscriptionEntityDTO cancelSubscription(String ownerEmail) throws StripeException {
        Owner owner = ownerRepository.findByUsername(ownerEmail).get();
        SubscriptionEntity subscriptionEntity = owner.getSubscriptionEntity();
        Subscription subscription = Subscription.retrieve(subscriptionEntity.getSubscriptionId());
        Subscription deletedSubscription = subscription.cancel();
        owner.setSubscriptionEntity(null);
        ownerRepository.save(owner);
        subscriptionEntityRepository.delete(subscriptionEntity);

        return SubscriptionEntityToSubscriptionEntityDTO.instance.convert(new SubscriptionEntity());

    }

    public SubscriptionEntityDTO retreiveSubscription(RetreiveSubscriptionRequestDTO retreiveSubscriptionRequestDTO) throws StripeException {
        Owner owner = ownerRepository.findByUsername(retreiveSubscriptionRequestDTO.getOwnerEmail()).get();
        if (Objects.isNull(owner.getSubscriptionEntity())) {
            return new SubscriptionEntityDTO();
        }
        SubscriptionEntity subscriptionEntity = owner.getSubscriptionEntity();
        Subscription subscription = Subscription.retrieve(subscriptionEntity.getSubscriptionId());
        subscriptionEntity = populateSubscriptionEntity(subscription, subscriptionEntity);
        subscriptionEntityRepository.save(subscriptionEntity);
        return SubscriptionEntityToSubscriptionEntityDTO.instance.convert(subscriptionEntity);
    }

    public SubscriptionEntityDTO retryInvoice(SubscriptionRequestDTO subscriptionRequestDTO) throws Exception {
        Customer customer = Customer.retrieve(subscriptionRequestDTO.getCustomerId());
        Owner owner = ownerRepository.findByStripeCustomerId(subscriptionRequestDTO.getCustomerId()).get();

        try {
            // Set the default payment method on the customer
            setPaymentMethod(subscriptionRequestDTO, customer);
        } catch (CardException e) {
            // Since it's a decline, CardException will be caught
            return setErrorInSubscriptionEntity(e);
        }

        updateCustomerParams(subscriptionRequestDTO, customer);

        InvoiceRetrieveParams params = InvoiceRetrieveParams
                .builder()
                .addAllExpand(Arrays.asList("payment_intent"))
                .build();
        Subscription subscription = Subscription.retrieve(owner.getSubscriptionEntity().getSubscriptionId());
        Invoice invoice = Invoice.retrieve(
                subscription.getLatestInvoice(),
                params,
                null
        );
        if (!invoice.getStatus().contentEquals(STRIPE_PAID_INVOICE)) {
            throw new Exception("payment card not valid");
        }
        owner.getSubscriptionEntity().setLatestInvoiceStatus(invoice.getStatus());
        owner = ownerRepository.save(owner);
        return SubscriptionEntityToSubscriptionEntityDTO.instance.convert(owner.getSubscriptionEntity());
    }

    public byte[] returnDomainFile() throws IOException {
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        Path path = Paths.get(absolutePath + "\\src\\main\\resources\\apple-domain\\apple-developer-merchantid-domain-association");
        byte[] bytes = Files.readAllBytes(path);
        return bytes;
    }

    private SubscriptionEntity populateSubscriptionEntity(Subscription subscription, SubscriptionEntity subscriptionEntity) throws StripeException {
        Invoice invoice = getInvoice(subscription);
        subscriptionEntity.setSubscriptionId(subscription.getId());
        subscriptionEntity.setCreated(subscription.getCreated());
        subscriptionEntity.setLatestInvoiceStatus(invoice.getStatus());
        subscriptionEntity.setObject(subscription.getObject());
        subscriptionEntity.setStatus(subscription.getStatus());
        subscriptionEntity.setPeriodStart(subscription.getCurrentPeriodStart());
        subscriptionEntity.setPeriodEnd(subscription.getCurrentPeriodEnd());
        return subscriptionEntity;
    }

    private Invoice getInvoice(Subscription subscription) throws StripeException {
        Invoice invoice = Invoice.retrieve(subscription.getLatestInvoice());
        return invoice;
    }


    private StripeSessionCustomerIdDTO returnStripeSessionCustomerIdDTO(Owner owner) {
        StripeSessionCustomerIdDTO stripeSessionCustomerIdDTO = new StripeSessionCustomerIdDTO();
        stripeSessionCustomerIdDTO.setCustomerId(owner.getStripeCustomerId());
        return stripeSessionCustomerIdDTO;
    }

    private SubscriptionEntityDTO setErrorInSubscriptionEntity(CardException e) {
        SubscriptionEntityDTO subscriptionEntityDTO = new SubscriptionEntityDTO();
        subscriptionEntityDTO.setErrorMessage(e.getLocalizedMessage());
        return subscriptionEntityDTO;
    }

    private void setPaymentMethod(SubscriptionRequestDTO subscriptionRequestDTO, Customer customer) throws StripeException {
        PaymentMethod pm = PaymentMethod.retrieve(
                subscriptionRequestDTO.getPaymentMethodId()
        );
        pm.attach(
                PaymentMethodAttachParams
                        .builder()
                        .setCustomer(customer.getId())
                        .build()
        );
    }
}
