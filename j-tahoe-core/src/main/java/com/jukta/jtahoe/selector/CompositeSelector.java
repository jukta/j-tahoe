package com.jukta.jtahoe.selector;

import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JTag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sergey on 12/8/2017.
 */
public class CompositeSelector {

    List<GroupSelector> sels;

    private CompositeSelector(List<GroupSelector> sels) {
        this.sels = sels;
    }

    public Set<JTag> select(JElement t) {
        HashSet<JTag> tags = new HashSet<>();
        for (GroupSelector gs : sels) {
            tags.addAll(gs.select(t));
        }
        return tags;
    }

    public static CompositeSelector parse(String sel) {
        List<GroupSelector> sels = new ArrayList<>();
        for (String s: sel.split(",")) {
            sels.add(GroupSelector.parse(s.trim()));
        }
        return new CompositeSelector(sels);

    }

}
