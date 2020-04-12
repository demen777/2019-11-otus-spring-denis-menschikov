function confirmDeleteBook(bookId) {
    if(confirm("Вы действительно хотите удалить книгу с id="+bookId)) {
        window.location = "/book/delete?id="+bookId;
    }
}