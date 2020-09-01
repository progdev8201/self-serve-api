package com.service;

import com.model.entity.Bill;
import com.model.entity.Owner;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    @Value("${stripe.apiKey}")
    private String stripeAPIKey;

    public String createStripeAccount(Owner owner) throws StripeException {
        Stripe.apiKey = stripeAPIKey;


        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setEmail(owner.getUsername())
                        .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                        .setBusinessProfile(AccountCreateParams.BusinessProfile.builder().
                                setProductDescription("produitDescription").
                                setName(owner.getRestaurantList().get(0).getName()).
                                setSupportEmail(owner.getUsername()).build())
                        .setCapabilities(AccountCreateParams.Capabilities.builder().
                                setTransfers(AccountCreateParams.Capabilities.Transfers.builder().
                                        setRequested(true).build()).
                                setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build()).
                                build())
                        .setCompany(AccountCreateParams.Company.builder().
                                setName(owner.getRestaurantList().get(0).getName()).
                                setPhone("5143652481").
                                build())
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .build();

        Account account =Account.create(params);

        AccountLinkCreateParams accountLinkparams =
                AccountLinkCreateParams.builder()
                        .setAccount(account.getId())
                        .setRefreshUrl("https://example.com/reauth")
                        .setReturnUrl("https://example.com/return")
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();

        AccountLink accountLink = AccountLink.create(accountLinkparams);
        return accountLink.getUrl();
    }

    public String processPayment (String restaurentStripeAccount, Bill bill) throws StripeException {
        Stripe.apiKey = stripeAPIKey;


        ArrayList paymentMethodTypes = new ArrayList();
        paymentMethodTypes.add("card");


        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", paymentMethodTypes);
        params.put("amount", bill.getPrixTotal());
        params.put("currency", "cad");
        params.put("application_fee_amount", 0);
        Map<String, Object> transferDataParams = new HashMap<>();
        transferDataParams.put("destination", restaurentStripeAccount);
        params.put("transfer_data", transferDataParams);
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret();

    }
}
