package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class IconFactory {

    public static ImageIcon createReportsIcon(int size) {
        return createReportsIcon(size, new Color(155, 89, 182)); // Màu tím cho báo cáo
    }

    public static ImageIcon createReportsIcon(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Thiết lập chất lượng rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Vẽ icon báo cáo
        drawReportsIcon(g2d, size, color);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static void drawReportsIcon(Graphics2D g2d, int size, Color color) {
        float scale = size / 32.0f;
        int padding = (int) (4 * scale);
        int contentSize = size - (2 * padding);

        // Màu sắc
        Color mainColor = color;
        Color paperColor = Color.WHITE;
        Color chartColor = new Color(52, 152, 219);
        Color chartColor2 = new Color(46, 204, 113);
        Color chartColor3 = new Color(241, 196, 15);

        // Vẽ tài liệu báo cáo (hình chữ nhật bo góc)
        g2d.setColor(paperColor);
        g2d.fill(new RoundRectangle2D.Float(
                padding,
                padding,
                contentSize,
                contentSize,
                contentSize * 0.2f,
                contentSize * 0.2f
        ));

        // Vẽ viền tài liệu
        g2d.setColor(mainColor);
        g2d.setStroke(new BasicStroke(1.5f * scale));
        g2d.draw(new RoundRectangle2D.Float(
                padding,
                padding,
                contentSize,
                contentSize,
                contentSize * 0.2f,
                contentSize * 0.2f
        ));

        // Vẽ các đường kẻ trên tài liệu
        g2d.setColor(new Color(200, 200, 200));
        g2d.setStroke(new BasicStroke(0.8f * scale));

        int lineCount = 5;
        int lineSpacing = contentSize / (lineCount + 1);
        for (int i = 1; i <= lineCount; i++) {
            int y = padding + lineSpacing * i;
            g2d.drawLine(padding + 4, y, padding + contentSize - 4, y);
        }

        // Vẽ biểu đồ nhỏ trên góc phải
        int chartSize = contentSize / 3;
        int chartX = padding + contentSize - chartSize - 2;
        int chartY = padding + 2;

        // Vẽ các cột biểu đồ
        int barWidth = chartSize / 4;

        // Cột 1
        g2d.setColor(chartColor);
        int bar1Height = (int) (chartSize * 0.6f);
        g2d.fillRect(chartX, chartY + chartSize - bar1Height, barWidth, bar1Height);

        // Cột 2
        g2d.setColor(chartColor2);
        int bar2Height = (int) (chartSize * 0.8f);
        g2d.fillRect(chartX + barWidth + 1, chartY + chartSize - bar2Height, barWidth, bar2Height);

        // Cột 3
        g2d.setColor(chartColor3);
        int bar3Height = (int) (chartSize * 0.4f);
        g2d.fillRect(chartX + 2 * (barWidth + 1), chartY + chartSize - bar3Height, barWidth, bar3Height);

        // Vẽ tiêu đề báo cáo (dòng đầu tiên đậm)
        g2d.setColor(mainColor);
        g2d.setStroke(new BasicStroke(1.2f * scale));
        int titleY = padding + lineSpacing;
        g2d.drawLine(padding + 4, titleY, padding + contentSize / 2, titleY);

        // Vẽ biểu tượng đồ thị nhỏ bên trái
        int graphX = padding + 4;
        int graphY = padding + contentSize / 2;
        int graphSize = contentSize / 4;

        g2d.setColor(chartColor);
        g2d.setStroke(new BasicStroke(1.5f * scale));

        // Vẽ đường đồ thị
        GeneralPath graph = new GeneralPath();
        graph.moveTo(graphX, graphY + graphSize);
        graph.lineTo(graphX + graphSize / 3, graphY + graphSize / 4);
        graph.lineTo(graphX + 2 * graphSize / 3, graphY + graphSize / 2);
        graph.lineTo(graphX + graphSize, graphY);

        g2d.draw(graph);

        // Vẽ các điểm dữ liệu
        g2d.setColor(chartColor2);
        g2d.fillOval(graphX + graphSize / 3 - 1, graphY + graphSize / 4 - 1, 3, 3);
        g2d.fillOval(graphX + 2 * graphSize / 3 - 1, graphY + graphSize / 2 - 1, 3, 3);
    }

    public static ImageIcon createInventoryIcon(int size) {
        return createInventoryIcon(size, new Color(230, 126, 34)); // Màu cam cho kho hàng
    }

    public static ImageIcon createInventoryIcon(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Thiết lập chất lượng rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Vẽ icon kho hàng
        drawInventoryIcon(g2d, size, color);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static void drawInventoryIcon(Graphics2D g2d, int size, Color color) {
        float scale = size / 32.0f;
        int padding = (int) (4 * scale);
        int contentSize = size - (2 * padding);

        // Màu sắc
        Color mainColor = color;
        Color shelfColor = new Color(139, 69, 19); // Màu gỗ
        Color boxColor1 = new Color(52, 152, 219);
        Color boxColor2 = new Color(46, 204, 113);
        Color boxColor3 = new Color(155, 89, 182);

        // Vẽ kệ kho hàng
        g2d.setColor(shelfColor);

        // Chân kệ
        int legWidth = (int) (contentSize * 0.1f);
        int legHeight = (int) (contentSize * 0.15f);
        g2d.fillRect(padding, padding + contentSize - legHeight, legWidth, legHeight);
        g2d.fillRect(padding + contentSize - legWidth, padding + contentSize - legHeight, legWidth, legHeight);

        // Mặt kệ
        g2d.fillRect(padding, padding + contentSize - legHeight - 2, contentSize, 3);

        // Tầng kệ thứ hai
        int shelf2Y = padding + contentSize * 2 / 3;
        g2d.fillRect(padding, shelf2Y, contentSize, 3);

        // Tầng kệ thứ ba
        int shelf3Y = padding + contentSize / 3;
        g2d.fillRect(padding, shelf3Y, contentSize, 3);

        // Vẽ các hộp hàng
        int boxSize = contentSize / 4;

        // Hộp trên tầng 1
        g2d.setColor(boxColor1);
        g2d.fillRect(padding + 4, shelf3Y - boxSize - 2, boxSize, boxSize);
        g2d.setColor(boxColor1.darker());
        g2d.drawRect(padding + 4, shelf3Y - boxSize - 2, boxSize, boxSize);

        // Hộp trên tầng 2
        g2d.setColor(boxColor2);
        g2d.fillRect(padding + contentSize / 2 - boxSize / 2, shelf2Y - boxSize - 2, boxSize, boxSize);
        g2d.setColor(boxColor2.darker());
        g2d.drawRect(padding + contentSize / 2 - boxSize / 2, shelf2Y - boxSize - 2, boxSize, boxSize);

        // Hộp trên tầng 3 (sàn)
        g2d.setColor(boxColor3);
        g2d.fillRect(padding + contentSize - boxSize - 4, padding + contentSize - legHeight - boxSize - 2, boxSize, boxSize);
        g2d.setColor(boxColor3.darker());
        g2d.drawRect(padding + contentSize - boxSize - 4, padding + contentSize - legHeight - boxSize - 2, boxSize, boxSize);

        // Vẽ nhãn trên hộp
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (6 * scale)));

        // Nhãn hộp 1
        String label1 = "A1";
        FontMetrics fm = g2d.getFontMetrics();
        int label1X = padding + 4 + (boxSize - fm.stringWidth(label1)) / 2;
        int label1Y = shelf3Y - boxSize / 2 + fm.getHeight() / 4;
        g2d.drawString(label1, label1X, label1Y);

        // Nhãn hộp 2
        String label2 = "B2";
        int label2X = padding + contentSize / 2 - boxSize / 2 + (boxSize - fm.stringWidth(label2)) / 2;
        int label2Y = shelf2Y - boxSize / 2 + fm.getHeight() / 4;
        g2d.drawString(label2, label2X, label2Y);

        // Nhãn hộp 3
        String label3 = "C3";
        int label3X = padding + contentSize - boxSize - 4 + (boxSize - fm.stringWidth(label3)) / 2;
        int label3Y = padding + contentSize - legHeight - boxSize / 2 + fm.getHeight() / 4;
        g2d.drawString(label3, label3X, label3Y);

        // Vẽ biểu tượng stack (chồng hộp)
        int stackX = padding + contentSize / 4;
        int stackY = padding + contentSize - legHeight - boxSize - 8;

        g2d.setColor(new Color(241, 196, 15));
        for (int i = 0; i < 3; i++) {
            g2d.fillRect(stackX, stackY - i * 2, boxSize / 2, boxSize / 3);
            g2d.setColor(new Color(241, 196, 15).darker());
            g2d.drawRect(stackX, stackY - i * 2, boxSize / 2, boxSize / 3);
            g2d.setColor(new Color(241, 196, 15));
        }
    }

// Phiên bản đơn giản cho icon nhỏ
    public static ImageIcon createSimpleReportsIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color mainColor = new Color(155, 89, 182);
        int padding = size / 6;
        int contentSize = size - 2 * padding;

        // Vẽ tài liệu đơn giản
        g2d.setColor(Color.WHITE);
        g2d.fillRect(padding, padding, contentSize, contentSize);

        g2d.setColor(mainColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRect(padding, padding, contentSize, contentSize);

        // Vẽ các đường kẻ
        for (int i = 1; i <= 3; i++) {
            int y = padding + contentSize * i / 4;
            g2d.drawLine(padding + 2, y, padding + contentSize - 2, y);
        }

        // Vẽ biểu đồ nhỏ
        int chartSize = contentSize / 2;
        int chartX = padding + contentSize - chartSize - 2;
        int chartY = padding + 2;

        g2d.setColor(new Color(52, 152, 219));
        g2d.fillRect(chartX, chartY + chartSize / 2, chartSize / 4, chartSize / 2);

        g2d.setColor(new Color(46, 204, 113));
        g2d.fillRect(chartX + chartSize / 3, chartY + chartSize / 4, chartSize / 4, chartSize * 3 / 4);

        g2d.setColor(new Color(241, 196, 15));
        g2d.fillRect(chartX + 2 * chartSize / 3, chartY, chartSize / 4, chartSize);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createSimpleInventoryIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color mainColor = new Color(230, 126, 34);
        int padding = size / 6;
        int contentSize = size - 2 * padding;

        // Vẽ kệ đơn giản
        g2d.setColor(new Color(139, 69, 19));
        g2d.setStroke(new BasicStroke(2.0f));

        // Chân kệ
        g2d.drawLine(padding, padding + contentSize, padding, padding + contentSize - 4);
        g2d.drawLine(padding + contentSize, padding + contentSize, padding + contentSize, padding + contentSize - 4);

        // Mặt kệ
        g2d.drawLine(padding, padding + contentSize - 4, padding + contentSize, padding + contentSize - 4);

        // Vẽ các hộp
        int boxSize = contentSize / 3;

        g2d.setColor(new Color(52, 152, 219));
        g2d.fillRect(padding + 2, padding + contentSize - boxSize - 6, boxSize, boxSize);

        g2d.setColor(new Color(46, 204, 113));
        g2d.fillRect(padding + contentSize / 2 - boxSize / 2, padding + contentSize - boxSize - 6, boxSize, boxSize);

        g2d.setColor(new Color(155, 89, 182));
        g2d.fillRect(padding + contentSize - boxSize - 2, padding + contentSize - boxSize - 6, boxSize, boxSize);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createSettingsIcon(int size) {
        return createSettingsIcon(size, new Color(52, 152, 219)); // Màu xanh chủ đạo
    }

    public static ImageIcon createSettingsIcon(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Thiết lập chất lượng rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Vẽ icon cài đặt
        drawSettingsIcon(g2d, size, color);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static void drawSettingsIcon(Graphics2D g2d, int size, Color color) {
        float scale = size / 32.0f;
        int padding = (int) (4 * scale);
        int contentSize = size - (2 * padding);

        // Màu sắc
        Color mainColor = color;
        Color gearColor = mainColor;
        Color innerCircleColor = Color.WHITE;
        Color screwColor = new Color(200, 200, 200);

        // Vẽ bánh răng ngoài
        drawGear(g2d,
                padding + contentSize / 2, padding + contentSize / 2,
                contentSize * 0.4f, contentSize * 0.3f, 8,
                gearColor, true);

        // Vẽ vòng tròn trong
        int innerCircleRadius = (int) (contentSize * 0.2f);
        g2d.setColor(innerCircleColor);
        g2d.fillOval(
                padding + contentSize / 2 - innerCircleRadius,
                padding + contentSize / 2 - innerCircleRadius,
                innerCircleRadius * 2,
                innerCircleRadius * 2
        );

        // Vẽ vòng tròn viền
        g2d.setColor(gearColor);
        g2d.setStroke(new BasicStroke(1.5f * scale));
        g2d.drawOval(
                padding + contentSize / 2 - innerCircleRadius,
                padding + contentSize / 2 - innerCircleRadius,
                innerCircleRadius * 2,
                innerCircleRadius * 2
        );

        // Vẽ ốc vít (dấu chấm)
        int screwRadius = (int) (contentSize * 0.05f);
        g2d.setColor(screwColor);
        g2d.fillOval(
                padding + contentSize / 2 - screwRadius,
                padding + contentSize / 2 - screwRadius,
                screwRadius * 2,
                screwRadius * 2
        );

        // Vẽ viền ốc vít
        g2d.setColor(gearColor.darker());
        g2d.setStroke(new BasicStroke(1 * scale));
        g2d.drawOval(
                padding + contentSize / 2 - screwRadius,
                padding + contentSize / 2 - screwRadius,
                screwRadius * 2,
                screwRadius * 2
        );
    }

    private static void drawGear(Graphics2D g2d, float centerX, float centerY,
            float outerRadius, float innerRadius, int teeth,
            Color color, boolean fill) {
        g2d.setColor(color);

        // Tạo hình bánh răng
        GeneralPath gear = new GeneralPath();

        float angleStep = (float) (2 * Math.PI / teeth);

        for (int i = 0; i < teeth; i++) {
            float angle1 = i * angleStep;
            float angle2 = angle1 + angleStep * 0.3f; // Răng nhô ra
            float angle3 = angle1 + angleStep * 0.7f; // Răng lõm vào
            float angle4 = angle1 + angleStep;

            // Điểm bắt đầu
            if (i == 0) {
                float x = centerX + (float) Math.cos(angle1) * innerRadius;
                float y = centerY + (float) Math.sin(angle1) * innerRadius;
                gear.moveTo(x, y);
            }

            // Điểm răng nhô ra
            float x2 = centerX + (float) Math.cos(angle2) * outerRadius;
            float y2 = centerY + (float) Math.sin(angle2) * outerRadius;
            gear.lineTo(x2, y2);

            // Điểm răng lõm vào
            float x3 = centerX + (float) Math.cos(angle3) * innerRadius;
            float y3 = centerY + (float) Math.sin(angle3) * innerRadius;
            gear.lineTo(x3, y3);

            // Điểm kết thúc răng
            float x4 = centerX + (float) Math.cos(angle4) * innerRadius;
            float y4 = centerY + (float) Math.sin(angle4) * innerRadius;
            gear.lineTo(x4, y4);
        }

        gear.closePath();

        if (fill) {
            g2d.fill(gear);
        } else {
            g2d.draw(gear);
        }
    }

    // Phiên bản đơn giản cho icon nhỏ
    public static ImageIcon createSimpleSettingsIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color mainColor = new Color(52, 152, 219);
        int padding = size / 6;
        int contentSize = size - 2 * padding;

        // Vẽ bánh răng đơn giản
        g2d.setColor(mainColor);

        // Vẽ vòng tròn ngoài
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawOval(padding, padding, contentSize, contentSize);

        // Vẽ các răng bánh răng
        int teeth = 6;
        double angleStep = 2 * Math.PI / teeth;
        int outerRadius = contentSize / 2;
        int innerRadius = outerRadius * 2 / 3;

        for (int i = 0; i < teeth; i++) {
            double angle = i * angleStep;
            double x1 = padding + contentSize / 2 + Math.cos(angle) * innerRadius;
            double y1 = padding + contentSize / 2 + Math.sin(angle) * innerRadius;
            double x2 = padding + contentSize / 2 + Math.cos(angle) * outerRadius;
            double y2 = padding + contentSize / 2 + Math.sin(angle) * outerRadius;

            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }

        // Vẽ vòng tròn trong
        g2d.drawOval(
                padding + contentSize / 2 - innerRadius / 2,
                padding + contentSize / 2 - innerRadius / 2,
                innerRadius,
                innerRadius
        );

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createCalendarIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ icon lịch cho đặt dịch vụ
        g2d.setColor(new Color(52, 152, 219));
        g2d.fill(new RoundRectangle2D.Float(4, 4, size - 8, size - 8, 8, 8));

        g2d.setColor(Color.WHITE);
        g2d.fill(new RoundRectangle2D.Float(6, 6, size - 12, size - 12, 6, 6));

        // Ngày
        g2d.setColor(new Color(52, 152, 219));
        g2d.setFont(new Font("Arial", Font.BOLD, size / 3));
        g2d.drawString("28", size / 2 - 6, size / 2 + 4);

        g2d.dispose();
        return new ImageIcon(image);
    }

    // Icon settings hiện đại với tool style
    public static ImageIcon createModernSettingsIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo gradient
        GradientPaint gradient = new GradientPaint(
                size / 2, 0, new Color(149, 165, 166),
                size / 2, size, new Color(127, 140, 141)
        );
        g2d.setPaint(gradient);

        int padding = size / 4;
        int contentSize = size - 2 * padding;

        // Vẽ nền tròn
        g2d.fill(new Ellipse2D.Double(padding, padding, contentSize, contentSize));

        // Vẽ bánh răng màu trắng
        g2d.setColor(Color.WHITE);
        drawSimpleGear(g2d, padding + contentSize / 2, padding + contentSize / 2, contentSize * 0.35f, 6);

        // Vẽ cờ lê/tuốc nơ vít
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2.0f));

        // Vẽ tay cầm cờ lê
        int handleY = padding + contentSize / 2;
        g2d.drawLine(padding + contentSize / 4, handleY, padding + contentSize * 3 / 4, handleY);

        // Vẽ đầu cờ lê
        int wrenchSize = contentSize / 4;
        g2d.drawRect(
                padding + contentSize / 2 - wrenchSize / 2,
                padding + contentSize / 2 - wrenchSize / 2,
                wrenchSize,
                wrenchSize
        );

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static void drawSimpleGear(Graphics2D g2d, float centerX, float centerY, float radius, int teeth) {
        GeneralPath gear = new GeneralPath();
        float angleStep = (float) (2 * Math.PI / teeth);

        for (int i = 0; i < teeth; i++) {
            float angle = i * angleStep;
            float toothLength = radius * 0.3f;

            // Điểm bắt đầu răng (bên trong)
            float x1 = centerX + (float) Math.cos(angle) * (radius - toothLength);
            float y1 = centerY + (float) Math.sin(angle) * (radius - toothLength);

            // Điểm nhô ra ngoài
            float x2 = centerX + (float) Math.cos(angle) * radius;
            float y2 = centerY + (float) Math.sin(angle) * radius;

            if (i == 0) {
                gear.moveTo(x1, y1);
            }

            gear.lineTo(x2, y2);

            // Điểm kết thúc răng (bên trong tiếp theo)
            float nextAngle = angle + angleStep;
            float x3 = centerX + (float) Math.cos(nextAngle) * (radius - toothLength);
            float y3 = centerY + (float) Math.sin(nextAngle) * (radius - toothLength);

            gear.lineTo(x3, y3);
        }

        gear.closePath();
        g2d.draw(gear);
    }

    // Icon settings với badge thông báo cập nhật
    public static ImageIcon createSettingsIconWithBadge(int size, boolean hasUpdate) {
        ImageIcon baseIcon = createModernSettingsIcon(size);

        if (!hasUpdate) {
            return baseIcon;
        }

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Vẽ icon gốc
        g2d.drawImage(baseIcon.getImage(), 0, 0, null);

        // Vẽ badge cập nhật màu xanh lá
        int badgeSize = size / 5;
        int badgeX = size - badgeSize - 1;
        int badgeY = 1;

        g2d.setColor(new Color(46, 204, 113));
        g2d.fillOval(badgeX, badgeY, badgeSize, badgeSize);

        // Vẽ dấu ✓
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(
                badgeX + badgeSize / 4,
                badgeY + badgeSize / 2,
                badgeX + badgeSize / 2,
                badgeY + badgeSize * 3 / 4
        );
        g2d.drawLine(
                badgeX + badgeSize / 2,
                badgeY + badgeSize * 3 / 4,
                badgeX + badgeSize * 3 / 4,
                badgeY + badgeSize / 4
        );

        g2d.dispose();
        return new ImageIcon(image);
    }

    // Icon settings với hiệu ứng xoay (cho animation)
    public static ImageIcon createAnimatedSettingsIcon(int size, float rotationAngle) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Áp dụng phép xoay
        g2d.rotate(rotationAngle, size / 2, size / 2);

        // Vẽ icon settings bình thường
        drawSettingsIcon(g2d, size, new Color(52, 152, 219));

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createHomeIcon(int size) {
        return createHomeIcon(size, new Color(41, 128, 185)); // Màu xanh chủ đạo
    }

    public static ImageIcon createHomeIcon(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Thiết lập chất lượng rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Vẽ icon ngôi nhà
        drawHomeIcon(g2d, size, color);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static void drawHomeIcon(Graphics2D g2d, int size, Color color) {
        // Tính toán tỷ lệ
        float scale = size / 32.0f;
        int padding = (int) (4 * scale);
        int iconSize = size - (2 * padding);

        // Màu sắc
        Color mainColor = color;
        Color roofColor = color.darker();
        Color windowColor = new Color(255, 255, 255, 200);
        Color doorColor = new Color(139, 69, 19); // Màu nâu cho cửa

        // Vẽ mái nhà (tam giác)
        int[] xPoints = {
            padding + iconSize / 2, // Đỉnh
            padding, // Góc trái dưới
            padding + iconSize // Góc phải dưới
        };
        int[] yPoints = {
            padding + (int) (iconSize * 0.3), // Đỉnh
            padding + (int) (iconSize * 0.6), // Góc trái dưới
            padding + (int) (iconSize * 0.6) // Góc phải dưới
        };

        g2d.setColor(roofColor);
        g2d.fillPolygon(xPoints, yPoints, 3);

        // Vẽ thân nhà (hình chữ nhật)
        int houseWidth = (int) (iconSize * 0.7);
        int houseHeight = (int) (iconSize * 0.5);
        int houseX = padding + (iconSize - houseWidth) / 2;
        int houseY = padding + (int) (iconSize * 0.45);

        g2d.setColor(mainColor);
        g2d.fillRect(houseX, houseY, houseWidth, houseHeight);

        // Vẽ cửa chính
        int doorWidth = (int) (houseWidth * 0.25);
        int doorHeight = (int) (houseHeight * 0.6);
        int doorX = houseX + (houseWidth - doorWidth) / 2;
        int doorY = houseY + houseHeight - doorHeight;

        g2d.setColor(doorColor);
        g2d.fillRect(doorX, doorY, doorWidth, doorHeight);

        // Vẽ tay nắm cửa
        g2d.setColor(Color.YELLOW);
        int knobSize = (int) (doorWidth * 0.15);
        g2d.fillOval(doorX + doorWidth - knobSize - 2, doorY + doorHeight / 2, knobSize, knobSize);

        // Vẽ cửa sổ
        int windowSize = (int) (houseWidth * 0.15);
        int window1X = houseX + (int) (houseWidth * 0.2);
        int window2X = houseX + (int) (houseWidth * 0.65);
        int windowY = houseY + (int) (houseHeight * 0.2);

        g2d.setColor(windowColor);
        g2d.fillRect(window1X, windowY, windowSize, windowSize);
        g2d.fillRect(window2X, windowY, windowSize, windowSize);

        // Vẽ khung cửa sổ
        g2d.setColor(Color.BLACK);
        g2d.drawRect(window1X, windowY, windowSize, windowSize);
        g2d.drawRect(window2X, windowY, windowSize, windowSize);
        g2d.drawLine(window1X + windowSize / 2, windowY, window1X + windowSize / 2, windowY + windowSize);
        g2d.drawLine(window1X, windowY + windowSize / 2, window1X + windowSize, windowY + windowSize / 2);
        g2d.drawLine(window2X + windowSize / 2, windowY, window2X + windowSize / 2, windowY + windowSize);
        g2d.drawLine(window2X, windowY + windowSize / 2, window2X + windowSize, windowY + windowSize / 2);

        // Vẽ ống khói
        int chimneyWidth = (int) (houseWidth * 0.15);
        int chimneyHeight = (int) (iconSize * 0.25);
        int chimneyX = houseX + (int) (houseWidth * 0.7);
        int chimneyY = padding + (int) (iconSize * 0.2);

        g2d.setColor(roofColor);
        g2d.fillRect(chimneyX, chimneyY, chimneyWidth, chimneyHeight);

        // Vẽ khói
        g2d.setColor(new Color(200, 200, 200, 150));
        int smokeX = chimneyX + chimneyWidth / 2;
        int smokeY = chimneyY;
        g2d.fillOval(smokeX - 2, smokeY - 6, 4, 4);
        g2d.fillOval(smokeX - 3, smokeY - 12, 6, 6);
        g2d.fillOval(smokeX - 2, smokeY - 18, 4, 4);
    }

    // Phiên bản đơn giản hơn cho icon nhỏ
    public static ImageIcon createSimpleHomeIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Màu sắc
        Color mainColor = new Color(41, 128, 185);
        Color roofColor = new Color(31, 97, 141);

        // Tính toán kích thước
        int padding = size / 8;
        int contentSize = size - 2 * padding;

        // Vẽ mái nhà (tam giác đơn giản)
        int[] xPoints = {
            padding + contentSize / 2,
            padding,
            padding + contentSize
        };
        int[] yPoints = {
            padding + contentSize / 3,
            padding + contentSize * 2 / 3,
            padding + contentSize * 2 / 3
        };

        g2d.setColor(roofColor);
        g2d.fillPolygon(xPoints, yPoints, 3);

        // Vẽ thân nhà
        int houseWidth = contentSize * 3 / 4;
        int houseHeight = contentSize / 2;
        int houseX = padding + (contentSize - houseWidth) / 2;
        int houseY = padding + contentSize / 2;

        g2d.setColor(mainColor);
        g2d.fillRect(houseX, houseY, houseWidth, houseHeight);

        // Vẽ cửa
        int doorWidth = houseWidth / 4;
        int doorHeight = houseHeight * 2 / 3;
        int doorX = houseX + (houseWidth - doorWidth) / 2;
        int doorY = houseY + houseHeight - doorHeight;

        g2d.setColor(new Color(120, 60, 20));
        g2d.fillRect(doorX, doorY, doorWidth, doorHeight);

        g2d.dispose();
        return new ImageIcon(image);
    }

    // Icon home với hiệu ứng hiện đại
    public static ImageIcon createModernHomeIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo gradient background
        GradientPaint gradient = new GradientPaint(
                size / 2, 0, new Color(52, 152, 219),
                size / 2, size, new Color(41, 128, 185)
        );
        g2d.setPaint(gradient);

        // Vẽ nền tròn
        g2d.fill(new Ellipse2D.Double(2, 2, size - 4, size - 4));

        // Vẽ icon ngôi nhà đơn giản màu trắng
        g2d.setColor(Color.WHITE);

        int padding = size / 4;
        int contentSize = size - 2 * padding;

        // Mái nhà
        int[] xPoints = {
            padding + contentSize / 2,
            padding,
            padding + contentSize
        };
        int[] yPoints = {
            padding + contentSize / 3,
            padding + contentSize * 2 / 3,
            padding + contentSize * 2 / 3
        };

        g2d.fillPolygon(xPoints, yPoints, 3);

        // Thân nhà
        int houseWidth = contentSize * 2 / 3;
        int houseHeight = contentSize / 3;
        int houseX = padding + (contentSize - houseWidth) / 2;
        int houseY = padding + contentSize * 2 / 3;

        g2d.fillRect(houseX, houseY, houseWidth, houseHeight);

        g2d.dispose();
        return new ImageIcon(image);
    }

    // Icon home với badge thông báo (cho notification)
    public static ImageIcon createHomeIconWithBadge(int size, boolean hasNotification) {
        ImageIcon baseIcon = createModernHomeIcon(size);

        if (!hasNotification) {
            return baseIcon;
        }

        // Tạo badge thông báo
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Vẽ icon gốc
        g2d.drawImage(baseIcon.getImage(), 0, 0, null);

        // Vẽ badge đỏ
        int badgeSize = size / 4;
        int badgeX = size - badgeSize - 2;
        int badgeY = 2;

        g2d.setColor(Color.RED);
        g2d.fillOval(badgeX, badgeY, badgeSize, badgeSize);

        // Vẽ chấm trắng
        g2d.setColor(Color.WHITE);
        g2d.fillOval(badgeX + badgeSize / 4, badgeY + badgeSize / 4, badgeSize / 2, badgeSize / 2);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createSalesIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Setup graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw icon
        g2.setColor(new Color(76, 175, 80));
        g2.fill(new RoundRectangle2D.Float(4, 6, size - 8, size - 12, 8, 8));

        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Float(10, 10, 4, 4));
        g2.fill(new Ellipse2D.Float(18, 10, 4, 4));

        g2.setStroke(new BasicStroke(2));
        g2.drawLine(8, 18, 24, 18);
        g2.drawLine(8, 22, 20, 22);

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createPasswordIcon(int size) {
        // Tạo icon đơn giản cho đổi mật khẩu
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Vẽ icon đơn giản (ổ khóa)
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(size / 4, size / 2, size / 2, size / 3);
        g2d.drawArc(size / 4, size / 3, size / 2, size / 3, 0, 180);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createServicesIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Face
        g2.setColor(new Color(33, 150, 243));
        g2.fill(new Ellipse2D.Float(8, 4, size - 16, size - 16));

        // Mouth
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(12, 14, 20, 14);

        // Eyes
        g2.fill(new Ellipse2D.Float(13, 8, 2, 2));
        g2.fill(new Ellipse2D.Float(19, 8, 2, 2));

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createCustomersIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Heads
        g2.setColor(new Color(156, 39, 176));
        g2.fill(new Ellipse2D.Float(7, 6, 8, 8));
        g2.fill(new Ellipse2D.Float(17, 6, 8, 8));

        // Bodies
        g2.setColor(new Color(225, 190, 231));
        g2.fill(new Ellipse2D.Float(2, 18, 12, 12));
        g2.fill(new Ellipse2D.Float(18, 18, 12, 12));

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createStaffIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Head
        g2.setColor(new Color(255, 87, 34));
        g2.fill(new Ellipse2D.Float(11, 5, 10, 10));

        // Body
        g2.setColor(new Color(255, 204, 188));
        g2.fill(new Ellipse2D.Float(8, 20, 16, 12));

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createRevenueIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Chart background
        g2.setColor(new Color(76, 175, 80));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Float(4, 4, size - 8, size - 8, 4, 4));

        // Chart line
        g2.drawLine(8, 20, 12, 12);
        g2.drawLine(12, 12, 16, 18);
        g2.drawLine(16, 18, 20, 10);
        g2.drawLine(20, 10, 24, 16);

        // Data points
        g2.fill(new Ellipse2D.Float(7, 19, 2, 2));
        g2.fill(new Ellipse2D.Float(11, 11, 2, 2));
        g2.fill(new Ellipse2D.Float(15, 17, 2, 2));
        g2.fill(new Ellipse2D.Float(19, 9, 2, 2));
        g2.fill(new Ellipse2D.Float(23, 15, 2, 2));

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createStatsIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Bars
        g2.setColor(new Color(33, 150, 243));
        g2.fill(new RoundRectangle2D.Float(6, 16, 4, 10, 2, 2));

        g2.setColor(new Color(76, 175, 80));
        g2.fill(new RoundRectangle2D.Float(12, 10, 4, 16, 2, 2));

        g2.setColor(new Color(255, 152, 0));
        g2.fill(new RoundRectangle2D.Float(18, 6, 4, 20, 2, 2));

        g2.setColor(new Color(156, 39, 176));
        g2.fill(new RoundRectangle2D.Float(24, 12, 4, 14, 2, 2));

        // Baseline
        g2.setColor(Color.GRAY);
        g2.drawLine(4, 26, 28, 26);

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createLogoutIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.GRAY);
        g2.fill(new RoundRectangle2D.Float(8, 4, 16, 8, 2, 2));
        g2.fill(new RoundRectangle2D.Float(8, 20, 12, 8, 2, 2));

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createExitIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(244, 67, 54));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Float(6, 6, size - 12, size - 12, 4, 4));

        g2.drawLine(10, 10, 22, 22);
        g2.drawLine(22, 10, 10, 22);

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createHelpIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Circle
        g2.setColor(new Color(33, 150, 243));
        g2.fill(new Ellipse2D.Float(4, 4, size - 8, size - 8));

        // Question mark
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Float(14, 10, 4, 4));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(14, 16, 16, 16);
        g2.drawLine(16, 16, 16, 22);
        g2.drawLine(18, 22, 16, 22);

        g2.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon createAboutIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Circle
        g2.setColor(new Color(255, 152, 0));
        g2.fill(new Ellipse2D.Float(4, 4, size - 8, size - 8));

        // "i" symbol
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Float(14, 10, 4, 4));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(16, 16, 16, 22);

        g2.dispose();
        return new ImageIcon(image);
    }
}
