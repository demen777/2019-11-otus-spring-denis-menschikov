insert into acl_sid(id, principal, sid) values (1, 'ROLE_ADMIN');
insert into acl_sid(id, principal, sid) values (2, 'ROLE_OPERATOR');
insert into acl_sid(id, principal, sid) values (3, 'ROLE_USER');
insert into acl_sid(id, principal, sid) values (4, 'ROLE_SPECIAL_BOOK');

insert into acl_class(id, class) values (1, 'ru.otus.demen.books.model.Book');

insert into acl_object_identity(id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
values (1, 1, 1, null, 1, false);
insert into acl_object_identity(id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
values (2, 1, 2, null, 1, false);

-- War and Peace - not special
insert into acl_entry(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values (1, 1, 10, 1, 1, true, false, true);
insert into acl_entry(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values (2, 1, 10, 2, 1, true, false, true);
insert into acl_entry(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values (3, 1, 10, 3, 1, true, false, true);

-- Anna Karenina - special
insert into acl_entry(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values (4, 2, 10, 1, 1, true, false, true);
insert into acl_entry(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values (5, 2, 10, 2, 1, true, false, true);
insert into acl_entry(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values (6, 2, 10, 4, 1, true, false, true);

