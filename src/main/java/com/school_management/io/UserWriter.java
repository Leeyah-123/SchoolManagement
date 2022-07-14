package com.school_management.io;

import com.google.gson.Gson;
import com.school_management.models.Auth;
import com.school_management.utils.Constants;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserWriter {
    private static final Base64 base64 = new Base64();
    public static boolean contains = System.getProperty("user.dir").contains("/");

    public UserWriter() {
        String file_path;
        if (contains) {
            file_path = Constants.USERDATAFILE;
        } else {
            file_path = Constants.WINDOWSUSERDATAFILE;
        }
        Path path = Paths.get(file_path);
        if (!Files.isDirectory(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void writeToFIle(Auth auth) {
        Gson gson = new Gson();
        if (contains) {
            try (Writer writer = new FileWriter(Constants.USERDATAFILE)) {
                gson.toJson(auth, writer);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            try (Writer writer = new FileWriter(Constants.WINDOWSUSERDATAFILE)) {
                gson.toJson(auth, writer);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public static Auth getCurrentUser() throws DecoderException {
        String file_path = "";
        if (contains) {
            String[] dir = Constants.dir;
            file_path = "/" + dir[1] + "/" + dir[2] + "/" + "userData";
        } else {
            String[] dir = Constants.windowsDir;
            file_path = dir[0] + "\\\\" + dir[1] + "\\\\" + "userData";
        }
        Path path = Paths.get(file_path);
        if (!Files.isDirectory(path)) {
            try {
                Files.createDirectory(path);
                System.out.println("Created Folder");
                File myObj;
                if (contains) {
                    myObj = new File(file_path + "/currentUser.sm");
                } else {
                    myObj = new File(file_path + "\\\\currentUser.sm");
                }

                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        Auth auth = new Auth();
        if (contains) {
            try (Reader reader = new FileReader(Constants.USERDATAFILE)) {
                auth = gson.fromJson(reader, Auth.class);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            try (Reader reader = new FileReader(Constants.WINDOWSUSERDATAFILE)) {
                auth = gson.fromJson(reader, Auth.class);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }

        String decodedUser;
        String decodedPassword;
        String decodedSelection;
        if (auth != null) {
            decodedUser = new String(base64.decode(auth.getEmail().getBytes()));
            decodedPassword = new String(base64.decode(auth.getPassword().getBytes()));
            decodedSelection = new String(base64.decode(auth.getSelection().getBytes()));
            return new Auth(decodedUser, DigestUtils.shaHex(decodedPassword), decodedSelection);
        }
        return null;
    }

    public static Boolean getUserSelection(){
        Auth auth;
        try {
            auth = getCurrentUser();
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        if (auth!=null) {
            String decodedSelection = auth.getSelection();
            return Boolean.parseBoolean(decodedSelection);
        }
        return false;
    }

    public static void encodeUserToFile(String email, String password, Boolean selection) {
        String encodedUser = new String(base64.encode(email.getBytes()));
        String encodedPassword = new String(base64.encode(password.getBytes()));
        String rememberMe = new String(base64.encode((selection).toString().getBytes()));
        writeToFIle(new Auth(encodedUser, DigestUtils.shaHex(encodedPassword), rememberMe));
    }

    public static void clearCurrentUser() {
        if (contains) {
            try (Writer writer = new FileWriter(Constants.USERDATAFILE)) {
                writer.write("");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            try (Writer writer = new FileWriter(Constants.WINDOWSUSERDATAFILE)) {
                writer.write("");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
