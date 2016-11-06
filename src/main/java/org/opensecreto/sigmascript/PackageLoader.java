package org.opensecreto.sigmascript;

import org.opensecreto.sigmascript.context.item.Class;
import org.opensecreto.sigmascript.exceptions.ClassNotFoundException;

public interface PackageLoader {

    public Class findClass(String name) throws ClassNotFoundException;

}
