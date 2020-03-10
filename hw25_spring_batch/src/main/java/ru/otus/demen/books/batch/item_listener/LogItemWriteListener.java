package ru.otus.demen.books.batch.item_listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LogItemWriteListener implements ItemWriteListener<Object> {
    @Override
    public void beforeWrite(@NonNull List items) {
    }

    @Override
    public void afterWrite(@NonNull List items) {
        log.info("afterWrite {}", items);
    }

    @Override
    public void onWriteError(@NonNull Exception exception, @NonNull List items) {
        log.info("onWriteError error={} items={}", exception, items);
    }
}
