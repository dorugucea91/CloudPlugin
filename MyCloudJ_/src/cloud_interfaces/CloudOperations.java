package cloud_interfaces;

import java.util.List;

public interface CloudOperations {
	public void login() throws CloudException;
	
	public CloudCapabilities getCloudCapabilities();
	
	public void disconnect() throws CloudException;

	public void downloadFile(String cloudPath, String localPath, CloudTransferCallback callback)
			throws CloudException;

	public void downloadFolder(String cloudPath, String localPath, CloudTransferCallback callback)
			throws CloudException;

	public void uploadFile(String localPath, String cloudPath, CloudTransferCallback callback)
			throws CloudException;

	public void uploadFolder(String localPath, String cloudPath, CloudTransferCallback callback)
			throws CloudException;
	
	public boolean mkdir(String cloudPath) throws CloudException;
	
	public boolean rename(String cloudPath, String newName) throws CloudException;
	
	public boolean deleteFile(String cloudPath) throws CloudException;

	public boolean isFile(String name) throws CloudException;
	
	public List<CloudFile> listFiles(String directoryPath)
			throws CloudException;
	
	public String getHomeDirectory() throws CloudException;
}