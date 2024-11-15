package com.demo.web.service;

import com.demo.web.model.Contact;
import com.demo.web.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Boolean saveContact(Contact contact) {
        try {
            // Lưu thông tin liên hệ vào cơ sở dữ liệu
            contactRepository.save(contact);
            return true;  // Trả về true nếu lưu thành công
        } catch (Exception e) {
            // Nếu có lỗi, log và trả về false
            e.printStackTrace();
            return false;
        }
    }

    public List<Contact> getLatestContacts() {

        // Lấy toàn bộ dữ liệu, sắp xếp theo createDate giảm dần
        List<Contact> latestContacts = contactRepository.findAll(Sort.by(Sort.Order.desc("createDate")));
        return latestContacts;
    }
}
