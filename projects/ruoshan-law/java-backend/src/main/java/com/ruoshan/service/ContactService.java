package com.ruoshan.service;

import com.ruoshan.entity.Contact;
import com.ruoshan.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepository;
    
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }
}
