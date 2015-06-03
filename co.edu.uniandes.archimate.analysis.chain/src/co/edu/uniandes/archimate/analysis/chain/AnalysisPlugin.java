package co.edu.uniandes.archimate.analysis.chain;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class AnalysisPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "co.edu.uniandes.archimate.analysis.chain";

    /**
     * The shared instance
     */
    public static AnalysisPlugin INSTANCE;
    

    public AnalysisPlugin() {
        INSTANCE = this;
    }
}