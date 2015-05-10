package generalUtils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class GeneralUtility {
	private static String OS;
		
	 /*
     * This function only adds unique children nodes to the parent node.
     * 
     * Parameters:
     * 
     * parentNode		:		Node to which children has to be added
     * childNode		:		Node to be added to the parent Node
     * model			:		JTree model
     * 
     */
    public static void addUniqueNode(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode childNode, DefaultTreeModel model) {
        // Check each node
        boolean isUnique = true;
        for (int i = 0; i < model.getChildCount(parentNode); i++)
        {
            Object compUserObj = ((DefaultMutableTreeNode) model.getChild(parentNode, i)).getUserObject();
            if (compUserObj.equals(childNode.getUserObject()))
            {
                isUnique = false;
                break;
            }
        }

        // If Unique, insert
        if(isUnique)
            model.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
    }
    
    /*
	 * Function to open Dropbox App URL in the default browser for user authentication
	 * 
	 * Parameters:
	 * 
	 * String url	:	MyCloudJ App url to be opened in the default browser
	 * 
	 * This function is called from the MyCloudJ_ class
	*/
	public static void openDefaultBrowser(String url) throws CloudException {
		String error = "Error opening browser:";
		
		if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
            	// opens the url in the browser
                desktop.browse(new URI(url));
            } catch (IOException e1) {
            	error = error.concat(" IOException " + e1.getMessage());
            	throw (new CloudException(error));
            } catch (URISyntaxException e2) {
            	error = error.concat(" Invalid URI " + e2.getMessage());
            	throw (new CloudException(error));
            }
        }
		else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e3) {
            	error = error.concat(" IOException " + e3.getMessage());
            	throw (new CloudException(error));
            }
        }
	}

	public static String getOS() {
		if (OS != null)
			return OS;
		
		return System.getProperty("os.name").toLowerCase();
	}

	public void setOS(String oS) {
		OS = oS;
	}
}
