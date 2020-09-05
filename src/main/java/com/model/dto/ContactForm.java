package com.model.dto;

import com.model.enums.ContactType;

public class ContactForm {
    private ContactType contactType;
    private String comment;

    public ContactForm(ContactType contactType, String comment) {
        this.contactType = contactType;
        this.comment = comment;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ContactForm{" +
                "contactType=" + contactType +
                ", comment='" + comment + '\'' +
                '}';
    }
}
