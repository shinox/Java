import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class PasswordGenerator {
	private static Text rpassLengthTxt;
	private static Text rpwCountText;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(578, 436);
		shell.setText("RPG -> (Random Password Generator)");
		shell.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(shell, SWT.BORDER | SWT.SHADOW_IN | SWT.CENTER);
		lblNewLabel.setText("Slide to select desired password length:");
		new Label(shell, SWT.NONE);
		
		final Scale rpassLengthChooser = new Scale(shell, SWT.NONE);
		rpassLengthChooser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				rpassLengthTxt.setText("" + rpassLengthChooser.getSelection());
			}
		});
		rpassLengthChooser.setMaximum(64);
		rpassLengthChooser.setMinimum(8);
		GridData gd_rpassLengthChooser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_rpassLengthChooser.widthHint = 344;
		rpassLengthChooser.setLayoutData(gd_rpassLengthChooser);
		
		rpassLengthTxt = new Text(shell, SWT.BORDER);
		GridData gd_rpassLengthTxt = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_rpassLengthTxt.widthHint = 56;
		rpassLengthTxt.setLayoutData(gd_rpassLengthTxt);
		rpassLengthTxt.setText("" + rpassLengthChooser.getMinimum());
		
		Label lblPasswordCount = new Label(shell, SWT.BORDER | SWT.SHADOW_IN | SWT.CENTER);
		lblPasswordCount.setText("Password Count:");
		new Label(shell, SWT.NONE);
		
		final Scale rpwCountChooser = new Scale(shell, SWT.NONE);
		rpwCountChooser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				rpwCountText.setText("" + rpwCountChooser.getSelection());
			}
		});
		rpwCountChooser.setMaximum(10);
		rpwCountChooser.setMinimum(1);
		GridData gd_rpwCountChooser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_rpwCountChooser.widthHint = 178;
		rpwCountChooser.setLayoutData(gd_rpwCountChooser);
		
		rpwCountText = new Text(shell, SWT.BORDER);
		GridData gd_rpwCountText = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_rpwCountText.widthHint = 53;
		rpwCountText.setLayoutData(gd_rpwCountText);
		rpwCountText.setText("" + rpwCountChooser.getMinimum());
		
		final Label lblClipCopyStatus = new Label(shell, SWT.NONE);
		lblClipCopyStatus.setForeground(SWTResourceManager.getColor(0, 204, 0));
		GridData gd_lblClipCopyStatus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblClipCopyStatus.widthHint = 343;
		lblClipCopyStatus.setLayoutData(gd_lblClipCopyStatus);

		
		Button rpwGenButton = new Button(shell, SWT.NONE);
		rpwGenButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		rpwGenButton.setText("Generate");
		
		final List rpwList = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		rpwList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Clipboard clipboard = new Clipboard(display);
				int index = rpwList.getFocusIndex();
				clipboard.setContents(new Object[] { rpwList.getItem(index) }, new Transfer[] { TextTransfer.getInstance() });
				String suffix;
				switch (index + 1) {
					case 1:
						suffix = "st";
						break;
					case 2:
						suffix = "nd";
						break;
					case 3:
						suffix = "rd";
						break;
					default:
						suffix = "th";
						break;
						
				}
				lblClipCopyStatus.setText("C-O-P-I-E-D: -> " + (index + 1) + suffix + " password from top");
			}
		});
		rpwList.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));	
		GridData gd_rpwList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_rpwList.widthHint = 549;
		gd_rpwList.heightHint = 253;
		rpwList.setLayoutData(gd_rpwList);	
		
		
		rpwGenButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lblClipCopyStatus.setText("");
				rpwList.removeAll();
 				/*
				 * This is how many times we want to generate rpasses
				 */
				for (int i = 0; i < Integer.valueOf(rpwCountText.getText()); i++) {
					
					PassGen passGen = new PassGen(Integer.valueOf(rpassLengthTxt.getText()));				
					rpwList.add(passGen.getPassword());					
				}
			}
		});


		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
