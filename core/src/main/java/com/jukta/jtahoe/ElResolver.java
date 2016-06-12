package com.jukta.jtahoe;

import de.odysseus.el.util.SimpleResolver;

import javax.el.ELContext;

/**
 * @author Sergey Sidorov
 */
public class ElResolver extends SimpleResolver {

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (base instanceof Attrs) {
            Attrs attrs = (Attrs) base;
            context.setPropertyResolved(true);
            return attrs.get(property.toString());
        }
        return super.getValue(context, base, property);
    }

}
