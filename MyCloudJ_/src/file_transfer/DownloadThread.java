package file_transfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import general.GeneralUtility;
import ij.io.Opener;

import javax.swing.SwingWorker;

import CloudGui.TransferProgressTable.UpdatableTableModel;
import cloud_interfaces.CloudException;
import cloud_interfaces.CloudOperations;
import cloud_interfaces.CloudTransferCallback;
import cloud_interfaces.CloudTransferStatus;

public class DownloadThread extends SwingWorker<Void, Void> implements
		CloudTransferCallback {
	private CloudOperations cloudHandler;
	private TransferTask task;
	private UpdatableTableModel model;
	private int transferId;
	private String currFile;

	public DownloadThread(TransferTask task, CloudOperations cloudHandler,
			UpdatableTableModel model, int transferId) {
		this.cloudHandler = cloudHandler;
		this.task = task;
		this.model = model;
		this.transferId = transferId;

		this.addPropertyChangeListener(new PropertyChange());
	}

	class PropertyChange implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("progress"))
				model.updateTransferStatus(getTransferId(),
						(int) evt.getNewValue(), currFile, false);
		}
	}

	@Override
	public void statusCallback(CloudTransferStatus transferStatus) {
		int fraction = transferStatus.getFraction();
		currFile = transferStatus.getCurrFile();
		firePropertyChange("progress", 0, fraction);
	}

	@Override
	public Void doInBackground() throws CloudException {
		String sourcePath = "";
		String destPath = "";
		boolean isFileDownload = false;
		String downloadType = ""; // file/folder
		try {
			// save task parameters
			sourcePath = task.getSourcePath();
			destPath = task.getDestinationPath();
			isFileDownload = cloudHandler.isFile(sourcePath);
			downloadType = isFileDownload ? "file" : "folder";

			// start the download
			if (isFileDownload)
				cloudHandler.downloadFile(sourcePath, destPath, this);
			else
				cloudHandler.downloadFolder(sourcePath, destPath, this);
			
			// TODO: open file from the progress table
			//if (isFileDownload)
			//	openFile(task);
		} catch (CloudException e) {
			e.printStackTrace();
			//TODO: DownloadThread should't know about RowData
			model.getRows().get(transferId).setError(e.getCloudError());
			model.updateTransferStatus(transferId, 0, "", true);
		}

		return null;
	}

	private void openFile(TransferTask task) {
		String fileName;

		fileName = GeneralUtility.getLastComponentFromPath(
				task.getSourcePath(), "/");
		fileName = GeneralUtility.getSystemSeparator() + fileName;
		Opener openfile = new Opener();
		openfile.open(task.getDestinationPath() + fileName);
	}

	public TransferTask getTask() {
		return task;
	}

	public void setTask(TransferTask task) {
		this.task = task;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}
}
