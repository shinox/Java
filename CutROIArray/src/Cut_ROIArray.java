import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.gui.GenericDialog;
import ij.gui.Roi;
import ij.plugin.PlugIn;
import ij.plugin.frame.RoiManager;

public class Cut_ROIArray implements PlugIn {

		
    @Override
    public void run(String arg) {
		
		int extra_pixels = 2
		  , blurSigma    = 2
		  , medianRadius = 10
		  ;		
		
		ImagePlus imp = IJ.getImage();
		
		/*
		// Select Open Image
		String filePath = new OpenDialog("Select an image file").getPath();
		// Open the selected file.
		ImagePlus imp = new Opener().openImage(filePath);
		// Display the ImagePlus.
		imp.show();						       	        		  				 		
		*/
		ImagePlus imp2 = imp.duplicate();
		imp2.setTitle("edge discovery");
		imp2.show(); // Show the duplicated image
		
		//IJ.run(imp2, "Median...", "radius=2");
		IJ.run(imp2, "16-bit", "");
		IJ.run(imp2, "Sharpen", "");
		IJ.run(imp2, "Despeckle", "");		
		IJ.run(imp2, "Median...", "radius=" + medianRadius);		
		IJ.run(imp2, "Gaussian Blur...", "sigma=" + blurSigma);
		
		//IJ.setAutoThreshold(imp2, "Default dark");
		IJ.setRawThreshold(imp, 223, 255, null);
		Prefs.blackBackground = false;
		IJ.run(imp2, "Convert to Mask", "");
		
		IJ.run(imp2, "Find Edges", "");				
		IJ.run(imp2, "Analyze Particles...", "add");
		
		RoiManager manager = RoiManager.getInstance();		
		//manager.reset();
		
		Roi[] rois = manager.getRoisAsArray(); // Have this just in case to edit rois
		int c = rois.length;
		
		int i = 0;
		for (i = 0; i < c; i++) {
			manager.select(i);
			IJ.run(imp2, "To Bounding Box", "");
			IJ.run(imp2, "Enlarge...", "enlarge="+extra_pixels+" pixel");
															
			manager.addRoi(imp2.getRoi());//manager.rename(i, "Region #"+IJ.pad(i+1,2));
			manager.rename(i, "Region #"+IJ.pad(i+1,2));
			manager.addNotify();
		}
		
		rois = manager.getRoisAsArray();	
		c = rois.length;
		
		while (i < c) {			
			manager.rename(i, "Square Region #"+IJ.pad(i+1,2));
			manager.addNotify();
			i++;
		}
		imp.setActivated();
		manager.runCommand(imp, "Show None");
		manager.runCommand(imp, "Show All");
		
		/* Dialog concept */
		/*
	    String title="Please Verify";
	    int width=512;
		int height=512;
		GenericDialog gd = new GenericDialog("Verification Of Image");
	    gd.addStringField("Title: ", title);
	    // gd.addNumericField("Width: ", width, 0);
	    // gd.addNumericField("Height: ", height, 0);
	    gd.showDialog();
	    if (gd.wasCanceled()) return;
	    title = gd.getNextString();
	    width = (int)gd.getNextNumber();
	    height = (int)gd.getNextNumber();
	    IJ.newImage(title, "8-bit", width, height, 1);
	    */
	}
        
}
