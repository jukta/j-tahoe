package com.jukta.jtahoe;

/**
 * @author Sergey Sidorov
 */
public interface DataHandler {

    Attrs getData(Attrs attrs);

    boolean async();

}
