package gui;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import input.FastaReader;
import input.FastaRecord;
import input.FastaWriter;
import input.Scorer;
import input.SequenceMatrix;

public class Gui {

	protected Shell shlFastaConservedDomain;
	private Text txt_min_indel;
	private Text txt_min_variation;
	private Text txt_min_category;
	private Text txt_max_indel;
	private Text txt_max_variation;
	private Text txt_max_category;
	private Text txt_file_path;
	private Label lbl_status;
	private static Scorer s;
	private static FastaReader fr;
	private static ArrayList<FastaRecord> records;
	private static SequenceMatrix sequences;
	private static int start_index = -1;
	private Text txtTargetFilePath;
	private Text text_window;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Gui window = new Gui();
			records = new ArrayList<>();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlFastaConservedDomain.open();
		shlFastaConservedDomain.layout();
		while (!shlFastaConservedDomain.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlFastaConservedDomain = new Shell();
		shlFastaConservedDomain.setSize(507, 300);
		shlFastaConservedDomain.setText("Fasta Conserved Domain Generator");
		
		txt_min_indel = new Text(shlFastaConservedDomain, SWT.BORDER);
		txt_min_indel.setText("0.000");
		txt_min_indel.setBounds(103, 23, 123, 19);
		
		txt_min_variation = new Text(shlFastaConservedDomain, SWT.BORDER);
		txt_min_variation.setText("0.000");
		txt_min_variation.setBounds(103, 64, 123, 19);
		
		txt_min_category = new Text(shlFastaConservedDomain, SWT.BORDER);
		txt_min_category.setText("0.000");
		txt_min_category.setBounds(103, 102, 123, 19);
		
		Button btnFind = new Button(shlFastaConservedDomain, SWT.NONE);
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fr = new FastaReader(txt_file_path.getText());
				try {
					records = fr.readRecords();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				sequences = new SequenceMatrix(records);
				s = new Scorer(Double.parseDouble(txt_min_indel.getText()),Double.parseDouble(txt_max_indel.getText()),Double.parseDouble(txt_min_variation.getText()), 
						Double.parseDouble(txt_max_variation.getText()), Double.parseDouble(txt_min_category.getText()),Double.parseDouble(txt_max_category.getText()),
						Integer.parseInt(text_window.getText()));
				start_index = s.score(sequences);
				if(start_index == -1)
				{
					lbl_status.setText("RECORDS DON'T MEET REQUIREMENT ");
				}
				else
				{
					lbl_status.setText("Start Index: " + start_index);	
				}
			}
		});
		
		
		btnFind.setBounds(212, 240, 94, 28);
		btnFind.setText("Find");
		
		Button btnGenerateReport = new Button(shlFastaConservedDomain, SWT.NONE);
		btnGenerateReport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FastaWriter fw = new FastaWriter(txtTargetFilePath.getText());
				try {
					fw.generate_fasta(start_index, s.getWindow(), records);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnGenerateReport.setBounds(322, 240, 119, 28);
		btnGenerateReport.setText("Generate Report");
		
		txt_max_indel = new Text(shlFastaConservedDomain, SWT.BORDER);
		txt_max_indel.setText("1.000");
		txt_max_indel.setBounds(336, 23, 128, 19);
		
		txt_max_variation = new Text(shlFastaConservedDomain, SWT.BORDER);
		txt_max_variation.setText("1.000");
		txt_max_variation.setBounds(337, 64, 127, 19);
		
		txt_max_category = new Text(shlFastaConservedDomain, SWT.BORDER);
		txt_max_category.setText("1.000");
		txt_max_category.setBounds(336, 102, 128, 19);
		
		Menu menu = new Menu(shlFastaConservedDomain, SWT.BAR);
		shlFastaConservedDomain.setMenuBar(menu);
		
		lbl_status = new Label(shlFastaConservedDomain, SWT.NONE);
		lbl_status.setBounds(10, 141, 231, 14);
		lbl_status.setText("Status");
		
		Label lblMinIndel = new Label(shlFastaConservedDomain, SWT.NONE);
		lblMinIndel.setBounds(10, 28, 59, 14);
		lblMinIndel.setText("Min Indel");
		
		Label lblMinVariation = new Label(shlFastaConservedDomain, SWT.NONE);
		lblMinVariation.setBounds(10, 67, 87, 14);
		lblMinVariation.setText("Min Variation");
		
		Label lblMinCategory = new Label(shlFastaConservedDomain, SWT.NONE);
		lblMinCategory.setBounds(10, 105, 87, 14);
		lblMinCategory.setText("Min Category");
		
		Label lblMaxIndel = new Label(shlFastaConservedDomain, SWT.NONE);
		lblMaxIndel.setBounds(247, 26, 59, 14);
		lblMaxIndel.setText("Max Indel");
		
		Label lblMaxVariation = new Label(shlFastaConservedDomain, SWT.NONE);
		lblMaxVariation.setText("Max Variation");
		lblMaxVariation.setBounds(247, 67, 94, 14);
		
		Label lblMaxCategory = new Label(shlFastaConservedDomain, SWT.NONE);
		lblMaxCategory.setText("Max Category");
		lblMaxCategory.setBounds(247, 105, 94, 14);
		
		Button btnSelectFile = new Button(shlFastaConservedDomain, SWT.NONE);
		btnSelectFile.setBounds(103, 240, 94, 28);
		btnSelectFile.setText("Select File");
		
		txt_file_path = new Text(shlFastaConservedDomain, SWT.BORDER);
		txt_file_path.setText("FILE PATH");
		txt_file_path.setBounds(10, 175, 487, 19);
		
		txtTargetFilePath = new Text(shlFastaConservedDomain, SWT.BORDER);
		txtTargetFilePath.setText("TARGET FILE PATH");
		txtTargetFilePath.setBounds(10, 200, 487, 19);
		
		text_window = new Text(shlFastaConservedDomain, SWT.BORDER);
		text_window.setText("20");
		text_window.setBounds(336, 138, 128, 19);
		
		Label lblWindowSize = new Label(shlFastaConservedDomain, SWT.NONE);
		lblWindowSize.setBounds(247, 141, 80, 14);
		lblWindowSize.setText("Window Size");
	}
}
