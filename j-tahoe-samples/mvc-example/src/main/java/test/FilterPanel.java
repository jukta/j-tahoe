package test;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.DataHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Sidorov
 */
public class FilterPanel implements DataHandler {

    @Override
    public Attrs getData(Attrs attrs) {
        return attrs.set("b", "hello from data handler");
    }

    @Override
    public boolean async() {
        return false;
    }
}
