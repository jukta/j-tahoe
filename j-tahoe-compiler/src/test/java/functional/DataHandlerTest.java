package functional;

import com.jukta.jtahoe.*;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JText;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @since 1.0
 */
public class DataHandlerTest extends AbstractTest {

    @Test
    public void dataHandler() {
        Block b = newBlockInstance("test.DataHandler_A");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public void getData(String dataHandler, Attrs attrs, Block.Callback callback) {
                assertEquals(attrs.get("a"), "A");
                assertEquals(dataHandler, "D");
                attrs.set("b", "B");
                callback.call();
            }

            @Override
            public void await() {}
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody().addElement(new JText("B"));
        assertEquals(expected, el);
    }

    @Test
    public void inheritanceDataHandler() {
        Block b = newBlockInstance("test.InheritanceDataHandler_B");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public void getData(String dataHandler, Attrs attrs, Block.Callback callback) {
                assertEquals(dataHandler, "D");
                attrs.set("b", "B");
                attrs.set("c", "C");
                attrs.set("d", "D");
                callback.call();
            }

            @Override
            public void await() {}
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody()
                .addElement(new JText("B"))
                .addElement(new JBody()
                        .addElement(new JBody().addElement(new JText("C")))
                        .addElement(new JText("D")));
        assertEquals(expected, el);
    }

    @Test
    public void compositionDataHandler() {
        Block b = newBlockInstance("test.CompositionDataHandler_B");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public void getData(String dataHandler, Attrs attrs, Block.Callback callback) {
                assertEquals(dataHandler, "D");
                attrs.set("b", "B");
                callback.call();
            }

            @Override
            public void await() {}
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody().addElement(new JBody().addElement(new JText("B")));
        assertEquals(expected, el);
    }

    @Test
    public void dataHandlerOverride() {
        Block b = newBlockInstance("test.DataHandlerOverride_B");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public void getData(String dataHandler, Attrs attrs, Block.Callback callback) {
                assertEquals(attrs.get("a"), "A");
                assertEquals(dataHandler, "D1");
                attrs.set("b", "B");
                callback.call();
            }

            @Override
            public void await() {}
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody().addElement(new JText("B"));
        assertEquals(expected, el);
    }

    @Test
    public void dataHandlerOverrideAsync() {
        Block b = newBlockInstance("test.DataHandlerAsync_C");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        String mainThread = Thread.currentThread().getName();
        DefaultDataHandlerProvider provider = new DefaultDataHandlerProvider(null) {
            @Override
            public DataHandler getDataHandler(String dataHandler, Attrs attrs) {
                if (dataHandler.equals("A")) {
                    return new DataHandler() {
                        @Override
                        public Attrs getData(Attrs attrs) {
                            assertNotEquals(mainThread, Thread.currentThread().getName());
                            return attrs.set("a", "AAA");
                        }

                        @Override
                        public boolean async() {
                            return true;
                        }
                    };
                } else if (dataHandler.equals("B")) {
                    return new DataHandler() {
                        @Override
                        public Attrs getData(Attrs attrs) {
                            assertNotEquals(mainThread, Thread.currentThread().getName());
                            return attrs.set("b", "BBB");
                        }

                        @Override
                        public boolean async() {
                            return true;
                        }
                    };

                } else if (dataHandler.equals("C")) {
                    return new DataHandler() {
                        @Override
                        public Attrs getData(Attrs attrs) {
                            assertNotEquals(mainThread, Thread.currentThread().getName());
                            return attrs.set("c", "CCC");
                        }

                        @Override
                        public boolean async() {
                            return true;
                        }
                    };
                }
                throw new RuntimeException();
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(5);
        provider.setExecutor(pool);
        attrs.setDataHandlerProvider(provider);

        JElement el = b.body(attrs);
        provider.await();

        JBody expected = new JBody()
                .addElement(new JBody()
                        .addElement(new JBody().addElement(new JText("AAA")))
                        .addElement(new JText("BBB")))
                .addElement(new JText("CCC"));
        assertEquals(expected, el);
        pool.shutdownNow();
    }

}
