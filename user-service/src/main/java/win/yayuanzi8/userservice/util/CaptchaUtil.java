package win.yayuanzi8.userservice.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Component
public class CaptchaUtil {
    private Random random = new Random();
    private int width = 80;//图片宽
    private int height = 38;//图片高
    private int lineNum = 3;//干扰线数量
    private int pointNum = 0;//噪点数量
    private int stringNum = 4;//随机产生字符数量
    private Font font = new Font("Times New Roman", Font.PLAIN, 24);
    private char[] index = {'a', 'A', '0'};//验证码索引

    /**
     * 生成验证码字符串
     */
    public char[] getValidateString() {
        char[] c = new char[stringNum];
        for (int i = 0; i < stringNum; i++) {
            int offset = random.nextInt(3);
            switch (offset) {
                case 0:
                case 1:
                    c[i] = (char) ((int) index[offset] + random.nextInt(26));
                    break;
                case 2:
                    c[i] = (char) ((int) index[offset] + random.nextInt(10));
                    break;
            }
        }
        return c;
    }

    /**
     * 生成图片
     */
    public void makeValidateImage(HttpServletRequest request, HttpServletResponse response) {
        //验证码
        char[] text = getValidateString();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setFont(font);
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, width, height);
        g.setColor(new Color(205, 205, 205));
        g.fillRect(0, 0, width, height);
        //绘制干扰线
        drawLine(g);
        //绘制噪点
        drawPoint(g);
        //绘制随机字符
        drawString(g, text);
        try {
            request.getSession().setAttribute("captcha", new String(text));
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g.dispose();
        }
    }

    /**
     * 绘制字符串
     */
    private void drawString(Graphics g, char[] text) {

       /* int x = random.nextInt(width);
        int y = ;*/
        //防止画图画出范围
        /*while(x>25){ x -= 20; }
        while(y<10){ y += 10; }*/
//        g.drawChars(text, 0, stringNum, x, y);
        int y;
        int Max = 30;
        int Min = 24;
        for (int i = 0; i < 4; i++) {
            g.setColor(new Color(255, 0, 0));
            y = (int) Math.round(Math.random() * (Max - Min) + Min);
//            System.out.println("i"+y);
            g.drawString(String.valueOf(text[i]), 15 * i + 10, y);
        }
    }

    /**
     * 绘制干扰线
     */
    private void drawLine(Graphics g) {
        for (int i = 0; i < lineNum; i++) {
            g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(0, y1, width, y2);
        }
    }

    /**
     * 绘制噪声点
     */
    private void drawPoint(Graphics g) {
        for (int i = 0; i < pointNum; i++) {
            g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawLine(x, y, x, y);
        }
    }
//    private static DefaultKaptcha captchagenerator;
}
