package com.jukta.jtahoe;

import de.odysseus.el.util.SimpleResolver;

import javax.el.ELContext;

/**
 * @author Sergey Sidorov
 */
public class ElResolver extends SimpleResolver {

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (base == null) {
            Attrs attrs = (Attrs) context.getVariableMapper().resolveVariable("attrs").getValue(context);
            return getValue(context, attrs, property);
        } else if (base instanceof Attrs) {
            Attrs attrs = (Attrs) base;
            context.setPropertyResolved(true);
            Object val = attrs.get(property.toString());
            if (val == null) {
                val = attrs.getAttribute(property.toString());
            }
            return val;
        }
        return super.getValue(context, base, property);
    }

}
