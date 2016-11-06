package org.opensecreto.sigmascript.context;

import org.opensecreto.sigmascript.context.item.Class;

import java.util.HashMap;

/**
 * One level of context.<br/><br/>
 * <p>
 * Example:<br>
 * We have ru.opensecreto.sigmascript<br/>
 * It will be 3 context levels:<br/>
 * 1. Package - ru<br/>
 * 2. Package - opensecreto<br/>
 * 3. Package - sigmascript<br/>
 */
public class Package {

    private final String levelName;
    private HashMap<String, Class> classes = new HashMap<>();
    private HashMap<String, Package> subPackages = new HashMap<>(8);

    /**
     * Creates package and subpackages.<br/>
     * Note subpackages will not be children of this package - the will by constructed as chain.<br/>
     * Example:<br/>
     * subpackages = {a, b, c}<br/>
     * Package tree: levelName: a - b - c
     *
     * @param levelName   Name of this package
     * @param subPackages Array of names of subpackages chain. Allowed to be empty.
     */
    public Package(String levelName, String[] subPackages) {
        this.levelName = levelName;

        if (subPackages.length > 0) {
            String subPackageName = subPackages[0];

            String[] subSubPackages = new String[subPackageName.length() - 1];
            System.arraycopy(subPackages, 1, subSubPackages, 1, subPackages.length - 1);

            this.subPackages.put(subPackageName, new Package(subPackageName, subSubPackages));
        }

    }

    public String getLevelName() {
        return levelName;
    }

}
