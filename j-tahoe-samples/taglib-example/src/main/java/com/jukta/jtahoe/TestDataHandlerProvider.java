package com.jukta.jtahoe;

/**
 * @since 1.0
 */
public class TestDataHandlerProvider implements DataHandlerProvider {

    @Override
    public void getData(String dataHandler, Attrs attrs, Block.Callback callback) {
        attrs.set("message", "Hello from JTahoe");
        callback.call();
    }

}
