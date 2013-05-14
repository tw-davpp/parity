package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ImageStore {

    private String url;

    public ImageStore(String url) {
        this.url = url;
    }

    public void storeImage(String sql, int imageSite) {
        try {
            String fileName = ImageDownload.download(url);
            byte[] content = getImageBytes(fileName);
            saveImageInDatabase(sql, content, imageSite);

            clean(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clean(String fileName) {
        File file = new File(fileName);
        if (file.exists())
            file.delete();
    }

    private byte[] getImageBytes(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fin = new FileInputStream(file);

        ByteBuffer nbf = ByteBuffer.allocate((int) file.length());
        byte[] array = new byte[1024];
        int length = 0;
        while ((length = fin.read(array)) > 0) {
            if (length != 1024)
                nbf.put(array, 0, length);
            else
                nbf.put(array);
        }
        fin.close();
        return nbf.array();
    }

    private boolean saveImageInDatabase(String sqlstr, byte[] in, int imageSite) {
        boolean flag = false;
        if (sqlstr == null)
            sqlstr = "select * from image";
        try {
            JDBCConnect jdbcConnect = JDBCConnect.getInstance();
            Connection connect = jdbcConnect.getConnection();

            Statement stmt = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(sqlstr);
            if (rs.next()) {
                rs.updateBytes(imageSite, in);     //imageSite表示更新第二个字段，即image这个字段
                rs.updateRow();
            } else {
                rs.moveToInsertRow();
                rs.updateBytes(imageSite, in);
                rs.insertRow();
            }
            rs.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
