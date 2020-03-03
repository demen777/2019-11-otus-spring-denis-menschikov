package ru.otus.demen.books.security;

import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Component;
import ru.otus.demen.books.model.Book;

@Component
public class BookAclCreatorImpl implements BookAclCreator {
    private final MutableAclService mutableAclService;
    private final Acl PARENT_ACL;

    public BookAclCreatorImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
        PARENT_ACL = mutableAclService.readAclById(
                new ObjectIdentityImpl("ru.otus.demen.books.model.__PARENT", 1L));
    }

    @Override
    public void createAcl(Book book) {
        MutableAcl acl = mutableAclService.createAcl(new ObjectIdentityImpl(Book.class, book.getId()));
        acl.setEntriesInheriting(true);
        acl.setParent(PARENT_ACL);
        mutableAclService.updateAcl(acl);
    }
}
