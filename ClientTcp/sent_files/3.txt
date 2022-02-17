package pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.file.PathUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        Path d1 = Paths.get("c:\\Users\\nick\\source\\java\\NIO_Project\\", "tst", "1");
        System.out.println(d1.getName(0));
        System.out.println(d1.getNameCount());
        System.out.println(d1.getRoot());
        System.out.println(d1.getParent());

        FileSystem fileSystem = d1.getFileSystem();
        /*for(FileStore fileStore : fileSystem.getFileStores()){
            System.out.println(fileStore.type());
            System.out.println(fileStore.getTotalSpace()/1024/1024/1024);
            System.out.println(fileStore.getUsableSpace()/1024/1024/1024);
            System.out.println(fileStore.getUnallocatedSpace()/1024/1024/1024);
        }
        System.out.println(d1.getFileName());*/
//        d1.toFile().mkdirs();
//        d1.toFile().delete();
        Files.createDirectories(d1);
        Path f1 = d1.resolve("file.txt");
        System.out.println(f1);
        if(!Files.exists(f1)){
            Files.createFile(f1);
        }
        Path link = d1.resolve("link.WEBLOC");
        if(!Files.exists(link)){
            Files.createFile(link);
        }
        System.out.println(link);
        Files.write(f1, Arrays.asList(
                "I am",
                "The new",
                "File"
        ), Charset.forName("cp1251"));
        String s = new String(Files.readAllBytes(f1), "cp1251");
        System.out.println(s);
        List<String> list = Files.readAllLines(f1, Charset.forName("cp1251"));
        System.out.println(list);
        Files.lines(f1, Charset.forName("cp1251")).forEach(System.out::println);
//        f1.toFile().renameTo(new File(f1.toFile().getParentFile().getParentFile(), "file2.txt"));
        Files.copy(f1, d1.resolve("file2.txt"), StandardCopyOption.REPLACE_EXISTING);
        System.out.println();
        Files.copy(f1, System.out);
        System.out.println(f1);
//        Files.setAttribute(f1, "unix:mode", 1);
        PosixFileAttributes posix = Files.readAttributes(f1, PosixFileAttributes.class);
        System.out.printf("%s %s %S%n",
                posix.owner().getName(), posix.group().getName(),
                PosixFilePermissions.toString(posix.permissions()).toLowerCase(Locale.ROOT));
        Set<PosixFilePermission> permissionsSet = PosixFilePermissions.fromString("rw-------");
        FileAttribute<Set<PosixFilePermission>> attribute = PosixFilePermissions.asFileAttribute(permissionsSet);
        Files.setPosixFilePermissions(f1, permissionsSet);

        int[] ints = {1, 3, 5, 7, 9};
        try(DataOutputStream dos = new DataOutputStream(
                new FileOutputStream("integers.txt"));
            DataInputStream dis = new DataInputStream(
                    new FileInputStream("integers.txt"))
        ){
            /*for(int i : ints) dos.writeInt(i);
            int n;
            while(true){
                n = dis.readInt();
                System.out.println(n);
            }*/
            dos.writeChars("qwerty");
            while (true) System.out.println(dis.readChar());
        }catch (Exception ignored){

        }

        User u1 = new User("U1", 12, true, User.Gender.OTHER);
       /* try(*//*ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream("users.txt"));*//*
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream("users.txt"))
        ){
//            oos.writeObject(u1);
            User u2 = (User) ois.readObject();
            System.out.println(u2);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        FileUtils.writeStringToFile(new File("apache.txt"), "EEEEEEEEEE", "utf-8");
        System.out.println(FilenameUtils.getExtension("file.txt"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(u1);
        FileUtils.writeStringToFile(new File("user.json"),
                json, "utf-8");
        json = FileUtils.readFileToString(new File("user.json"), "utf-8");
        User user = gson.fromJson(json, User.class);
        System.out.println(user);

        try(OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream("osw.txt"), "utf-8");
            InputStreamReader isr = new InputStreamReader(
                    new FileInputStream("osw.txt"), "utf-8"))
            {
                osw.write("filkfdsjfimlknfdgjfndiok");
                StringBuilder sb = new StringBuilder();
                int i=0;
                while((i= isr.read()) != -1)
                    sb.append((char) i);
                System.out.println(sb);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
