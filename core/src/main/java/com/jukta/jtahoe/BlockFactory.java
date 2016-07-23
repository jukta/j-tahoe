package com.jukta.jtahoe;

/**
 * @since 1.0
 */
public class BlockFactory {
    private ClassLoader classLoader;

    public BlockFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Block create(String blockName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (classLoader == null) {
            return (Block) Class.forName(blockName).newInstance();
        } else {
            return (Block) Class.forName(blockName, true, classLoader).newInstance();
        }
    }

}
