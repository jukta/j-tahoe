package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.DataHandlerProvider;

/**
 * @author Sergey Sidorov
 */
public interface DataHandlerProviderFactory {

    DataHandlerProvider create(Attrs attrs);

}
