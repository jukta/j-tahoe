package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.RuntimeBlockFactory;
import com.jukta.jtahoe.gen.xml.XthBlockModelProvider;

/**
 * Created by Dmitriy Dobrovolskiy on 04.04.2016.
 *
 * @since *.*.*
 */
public class JTahoeRuntimeViewResolver extends JTahoeViewResolver {

    public void afterPropertiesSet() throws Exception {
        if (getBlockFactory() == null) {
            setBlockFactory(new RuntimeBlockFactory(new XthBlockModelProvider()));
        }
    }

}