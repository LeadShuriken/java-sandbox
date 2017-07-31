package client;

import java.io.*;
import java.util.*;

/**
 * @author Deian Mishev INFM219 - Assignment
 * @version 1.0
 * @description Class handling chat Emoticons and Images
 */
public class EmojiHandling {
	private final File IMAGE_FOLDER;

	// Mapped Assets of the application //
	protected Hashtable<String, String> assets = new Hashtable<String, String>();
	protected ArrayList<String> emojiAssets;
	protected ArrayList<String> emojiIcons;

	public EmojiHandling(String imgFolder) {
		this.IMAGE_FOLDER = new File(imgFolder);
		this.emojiAssets = mapAssets(this.IMAGE_FOLDER);
		this.emojiIcons = new ArrayList<String>();
	}

	/**
	 * @method mapAssets
	 * @description Method mapping the file assets in the assets folder
	 * @param folder Folder where the physical(files) assets of the user are
	 * @return ArrayList<String>
	 * @throws RemoteExceptiopm
	 */
	private ArrayList<String> mapAssets(File folder) {
		ArrayList<String> pathString = new ArrayList<String>();
		for (final java.io.File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				mapAssets(fileEntry);
			} else {
				String[] parts = fileEntry.getPath().split("\\\\");
				String file = parts[parts.length - 1];
				pathString.add(file);
			}
		}
		return pathString;
	}

	/**
	 * @method getEmojis
	 * @description Method mapping the emojis(if any) in the last received message
	 * @param msg The last received message from the server
	 * @return String[]
	 */
	public String[] getEmojis(String msg) {
		HashSet<String> set = new HashSet<String>();
		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = msg.indexOf("~", lastIndex);
			if (lastIndex != -1) {
				String emoji = msg.substring(lastIndex + 1, lastIndex + 3);
				set.add(emoji);
				lastIndex += 1;
			}
		}
		return set.toArray(new String[set.size()]);
	}
}
