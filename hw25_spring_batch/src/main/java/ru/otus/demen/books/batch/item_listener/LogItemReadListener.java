package ru.otus.demen.books.batch.item_listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogItemReadListener implements ItemReadListener<Object> {
    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(@NonNull Object item) {
        log.info("afterRead {}", item);
    }

    @Override
    public void onReadError(@NonNull Exception ex) {
        log.info("onReadError", ex);
    }
}
