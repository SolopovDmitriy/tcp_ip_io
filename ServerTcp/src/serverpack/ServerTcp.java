package serverpack;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTcp {
    private final String address = "127.0.0.1";
    private int port;
    private InetAddress inetAddress;
    private InetSocketAddress inetSocketAddress;
    private String folder = "D:\\javaProject\\Java\\tcp_ip_io\\ServerTcp\\received_files\\";

    public ServerTcp(int port){
        this.port = port;
    }

    public void start(){
        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port); // Сервер создает экземпляр объекта ServerSocket,
                                                                // определяющий, по какому номеру порта должна происходить связь.
            System.out.println("Server->Start");

            socket = serverSocket.accept(); // Этот метод ожидает, пока клиент не подключится к серверу по указанному порту.
            System.out.println("Client connected");
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            while (true){
                String fileName = is.readUTF(); // чтение сообщения от клиента
                if(fileName.equals("list")){
                    File[] files = new File(folder).listFiles(); // files - список файлов
                    if(files.length == 0){ // если файлов в папке нет
                        os.writeUTF("Folder is empty!"); // отправляем клиенту сообщение что папка пустая
                        os.flush();
                    }else{
                        String filesList = Stream.of(files)
                                .map(File::getName)
                                .collect(Collectors.joining("\n")); // получить список файлов в виде строки
                        os.writeUTF(filesList); // отправляем клиенту список файлов
                        os.flush();
                    }
                }else {
                    int bytes = 0;
                    FileOutputStream fileOutputStream = new FileOutputStream(folder + fileName); // создаем файловый поток для создания файла
                    long size = is.readLong();     // читаем размер файла. Это число, которое отправил клиент
                    byte[] buffer = new byte[1024]; // создаем буфер
                    while (size > 0 && (bytes = is.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                        fileOutputStream.write(buffer, 0, bytes);
                        size -= bytes;     // уменьшаем размер файла на количество прочитанных байтов
                    }
                    fileOutputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
