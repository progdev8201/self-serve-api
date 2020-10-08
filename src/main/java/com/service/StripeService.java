package com.service;

import com.model.dto.StripeClientSecretDTO;
import com.model.entity.Bill;
import com.model.entity.Owner;
import com.repository.OwnerRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.ApplePayDomain;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.ApplePayDomainCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class StripeService {
    @Value("${stripe.apiKey}")
    private String stripeAPIKey;

    @Autowired
    private OwnerRepository ownerRepository;

    public String createStripeAccount(Owner thisowner) throws StripeException {
        Stripe.apiKey = stripeAPIKey;

        Owner owner =ownerRepository.findById(thisowner.getId()).get();


        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setEmail(owner.getUsername())
                        .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                        .setBusinessProfile(AccountCreateParams.BusinessProfile.builder().
                                setProductDescription("produitDescription").
                                setName(owner.getRestaurantList().get(0).getName()).
                                setSupportEmail(owner.getUsername()).build())
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.CARD_PAYMENTS)
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.TRANSFERS)
                        .setCompany(AccountCreateParams.Company.builder().
                                setName(owner.getRestaurantList().get(0).getName()).
                                setPhone("5143652481").
                                build())
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .build();

/*
        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setEmail(owner.getUsername())
                        .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.CARD_PAYMENTS)
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.TRANSFERS)
                        .build();

*/
        Account account = Account.create(params);

        AccountLinkCreateParams accountLinkparams =
                AccountLinkCreateParams.builder()
                        .setAccount(account.getId())
                        .setRefreshUrl("https://example.com/reauth")
                        .setReturnUrl("https://example.com/return")
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();
        owner.setStripeAccountId(account.getId());
        ownerRepository.save(owner);
        AccountLink accountLink = AccountLink.create(accountLinkparams);

        return accountLink.getUrl();
    }
    public void initApplePay() throws StripeException {
        Stripe.apiKey = stripeAPIKey;
        ApplePayDomainCreateParams params =
                ApplePayDomainCreateParams.builder()
                        .setDomainName("aae756e7fbd7.ngrok.io")
                        .build();

        ApplePayDomain domain = ApplePayDomain.create(params);

    }

    public StripeClientSecretDTO processPayment(String restaurentStripeAccount, Bill bill) throws StripeException {
        Stripe.apiKey = stripeAPIKey;

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(Math.round(bill.getPrixTotal()) * 100)
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
                        .setAmount(Math.round(bill.getPrixTotal()) * 100)
                        .setTransferData(PaymentIntentCreateParams.TransferData.builder().setDestination(restaurentStripeAccount).build())
                        // Verify your integration in this guide by including this parameter
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        StripeClientSecretDTO stripeClientSecretDTO = new StripeClientSecretDTO();
        stripeClientSecretDTO.setValue(paymentIntent.getClientSecret());
        return stripeClientSecretDTO;
    }
    public byte[] returnDomainFile() throws IOException {
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        Path path = Paths.get(absolutePath + "\\src\\main\\resources\\apple-domain\\apple-developer-merchantid-domain-association");
        byte[] bytes = Files.readAllBytes(path);
        return bytes;
    }
}
