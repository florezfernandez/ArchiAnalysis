package co.edu.uniandes.archimate.analysis;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ArchiAnalysisPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "co.edu.uniandes.archimate.analysis"; //$NON-NLS-1$

    /**
     * The shared instance
     */
    public static ArchiAnalysisPlugin INSTANCE;
    

    public ArchiAnalysisPlugin() {
        INSTANCE = this;
    }
}
