package com.jukta.jtahoe;

/**
 * @author Sergey Sidorov
 */
public interface DataHandlerProvider {

    void getData(String dataHandler, Attrs attrs, Block.Callback callback);

}
