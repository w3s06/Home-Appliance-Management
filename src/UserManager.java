import java.io.*;
import java.util.*;

/** 
 * Manages user accounts and authentication.
 * Handles login, registration, and user data storage.
 */

public class UserManager {
	private List<User> users = new ArrayList<>();
	
	public UserManager(String filePath) {
		loadUsers(filePath);
	}
	
	private void loadUsers(String filePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(", ");
				users.add(createUser(parts));
				}
		} catch (IOException e) {
			System.out.println("Error reading user file: " + e.getMessage());
		}
	}
	
	private User createUser(String[] userData) {
		return userData[6].equalsIgnoreCase("admin")
			? new Admin(userData[0], userData[1], userData[2], userData[3], userData[4], userData[5], userData[6])
			: new Customer(userData[0], userData[1], userData[2], userData[3], userData[4], userData[5], userData[6]);
	}
	
	public User authenticate(String username) {
		return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
	}
}
