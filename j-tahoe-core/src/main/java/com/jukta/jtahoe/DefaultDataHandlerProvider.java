package com.jukta.jtahoe;

import java.util.concurrent.Executor;
import java.util.concurrent.Phaser;

/**
 * @since 1.0
 */
public class DefaultDataHandlerProvider implements DataHandlerProvider {

    private DataHandler dataHandler;
    private Executor executor;
    private Phaser phaser = new Phaser();

    public DefaultDataHandlerProvider() {
    }

    public DefaultDataHandlerProvider(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void getData(String dataHandler, Attrs attrs, Block.Callback callback) {
        DataHandler handler = getDataHandler(dataHandler, attrs);
        if (handler != null) {
            execute(handler, attrs, callback);
        } else {
            callback.call();
        }
    }

    private void execute(final DataHandler handler, final Attrs attrs, final Block.Callback callback) {
        if (executor != null && handler.async()) {
            phaser.register();
            executor.execute(() -> {
                handle(handler, attrs, callback);
                phaser.arrive();
            });
        } else {
            handle(handler, attrs, callback);
        }
    }

    private void handle(DataHandler handler, Attrs attrs, Block.Callback callback) {
        Attrs a = handler.getData(attrs);
        if (attrs != a) attrs.setAll(a);
        callback.call();
    }

    public void await() {
        if (executor != null) {
            phaser.register();
            phaser.arriveAndAwaitAdvance();
        }
    }

    public DataHandler getDataHandler(String dataHandler, Attrs attrs) {
        return this.dataHandler;
    }
}
