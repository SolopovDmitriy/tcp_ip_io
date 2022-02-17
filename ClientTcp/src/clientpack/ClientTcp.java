package clientpack;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientTcp {
    private final String address = "127.0.0.1";
    private int serverPort;
    private InetAddress inetAddress;
    private InetSocketAddress inetSocketAddress;
    private String folder = "D:\\javaProject\\Java\\tcp_ip_io\\ClientTcp\\sent_files\\";

    public ClientTcp(int serverPort) {
        this.serverPort = serverPort;
    }

    void start(){
        Socket socket = null;
        try {
            inetAddress = InetAddress.getByName(address);
            inetSocketAddress = new InetSocketAddress(
                    inetAddress, serverPort
            );
            socket = new Socket(inetSocketAddress.getAddress(),
                    inetSocketAddress.getPort());
            System.out.println("Client Start");
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);
            while (true){
                System.out.println("Input name of the file. Or enter 'list' to see the list of files on the server");
                String line = sc.nextLine(); // ввод с клавиатуры имени файла или слова list
                if(line.equals("list")){ // Если ввели слово list
                    os.writeUTF("list"); // отправляем серверу слово list
                    String filesList = is.readUTF(); // читаем ответ от сервера. Сервер прислал список файлов одной строкой
                    System.out.println();
                    System.out.println("List: ");
                    System.out.println(filesList); // печатаем в консоль список файлов
                    System.out.println();
                }else{
                    int bytes = 0;
                    File file = new File(folder + line); // создаем файловую переменную
                    FileInputStream fileInputStream = new FileInputStream(file); // создаем файловый поток для чтения файла
                    os.writeUTF(line); // отправляем серверу имя файла
                    os.writeLong(file.length()); // отправляем серверу размер файла в байтах
                    byte[] buffer = new byte[1024]; // создаем буфер для чтения файла частями
                    while ((bytes=fileInputStream.read(buffer))!=-1){ // пока прочитанное из файла в буфер не пустое
                        os.write(buffer,0,bytes); // отправляем клиенту содержимое буфера
                        os.flush();
                    }
                    fileInputStream.close(); // закрываем файловый поток
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
