package Controller;

import Data.DataConnection;
import Model.LichSuGiaoDichTraTruoc;
import Service.LichSuGiaoDichTraTruocService;
import Service.ThongKeService;
import View.ThongKeView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ThongKeController {

    private final ThongKeView view;
    private final ThongKeService service;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Biến lưu trữ dữ liệu hiện tại
    private Map<String, Object> currentTongQuan;
    private List<Map<String, Object>> currentKhachHang;
    private List<Map<String, Object>> currentDoanhThu;
    private List<Map<String, Object>> currentDichVu;
    private Date currentFromDate;
    private Date currentToDate;
    private int currentYear;
    private List<Map<String, Object>> currentHoaDon;
    private List<LichSuGiaoDichTraTruoc> currentLichSuGiaoDich;

    public ThongKeController(ThongKeView view) {
        this.view = view;
        this.service = new ThongKeService();
        initController();
    }

// Thêm sự kiện double-click để xem chi tiết (trong initController)
    private void initController() {
        view.getBtnThongKe().addActionListener(e -> thongKe());
        view.getBtnXuatExcel().addActionListener(e -> xuatExcel());

        // Thêm sự kiện double-click cho table hóa đơn
        view.getTblHoaDon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = view.getTblHoaDon().getSelectedRow();
                    if (row >= 0) {
                        int maHoaDon = (Integer) view.getTblHoaDon().getValueAt(row, 0);
                        xemChiTietHoaDon(maHoaDon);
                    }
                }
            }
        });

        // Load danh sách năm
        loadDanhSachNam();

        // Thực hiện thống kê tự động khi mở form
        thongKe();

        // Sự kiện cho nút lọc lịch sử giao dịch
        view.getBtnLocLichSu().addActionListener(e -> locLichSuGiaoDich());

        // Sự kiện thay đổi combobox loại lọc
        view.getCboLoaiLocLichSu().addActionListener(e -> capNhatTrangThaiLocLichSu());

        // Thêm sự kiện cho nút xóa hóa đơn
        view.getBtnXoaHoaDon().addActionListener(e -> xoaHoaDon());

        // Thêm sự kiện cho nút xóa lịch sử giao dịch
        view.getBtnXoaLichSuGiaoDich().addActionListener(e -> xoaLichSuGiaoDich());
    }

    // Thêm phương thức xóa hóa đơn
    private void xoaHoaDon() {
        int selectedRow = view.getTblHoaDon().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        int maHoaDon = (Integer) view.getTblHoaDon().getValueAt(selectedRow, 0);
        String tenKhachHang = (String) view.getTblHoaDon().getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa hóa đơn #" + maHoaDon
                + " của khách hàng '" + tenKhachHang + "'?\n\n"
                + "Lưu ý: Tất cả chi tiết hóa đơn cũng sẽ bị xóa!",
                "Xác nhận xóa hóa đơn",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = service.xoaHoaDon(maHoaDon);
                if (success) {
                    JOptionPane.showMessageDialog(view, "Xóa hóa đơn thành công!");
                    // Cập nhật lại dữ liệu
                    thucHienThongKe(currentFromDate, currentToDate, currentYear);
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa hóa đơn thất bại!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi khi xóa hóa đơn: " + ex.getMessage());
            }
        }
    }

// Thêm phương thức xóa lịch sử giao dịch
    private void xoaLichSuGiaoDich() {
        int selectedRow = view.getTblLichSuGiaoDich().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn giao dịch cần xóa!");
            return;
        }

        int maLichSu = ((Number) view.getTblLichSuGiaoDich().getValueAt(selectedRow, 0)).intValue();
        String tenKhachHang = (String) view.getTblLichSuGiaoDich().getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa lịch sử giao dịch #" + maLichSu
                + " của khách hàng '" + tenKhachHang + "'?",
                "Xác nhận xóa giao dịch",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = service.xoaLichSuGiaoDich(maLichSu);
                if (success) {
                    JOptionPane.showMessageDialog(view, "Xóa giao dịch thành công!");
                    // Cập nhật lại dữ liệu
                    loadLichSuGiaoDich(null);
                    // Cập nhật thống kê tổng quan
                    thucHienThongKe(currentFromDate, currentToDate, currentYear);
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa giao dịch thất bại!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi khi xóa giao dịch: " + ex.getMessage());
            }
        }
    }
// Cập nhật phương thức trong ThongKeController

    private void capNhatTrangThaiLocLichSu() {
        String loaiLoc = (String) view.getCboLoaiLocLichSu().getSelectedItem();
        boolean enabled = "Theo tên KH".equals(loaiLoc); // Đổi thành "Theo tên KH"
        view.getTxtTenKHLocLichSu().setEnabled(enabled);

        if (!enabled) {
            view.getTxtTenKHLocLichSu().setText("");
            // Load lại tất cả dữ liệu khi chọn "Tất cả"
            loadLichSuGiaoDich(null);
        }
    }

// Cập nhật phương thức locLichSuGiaoDich()
    private void locLichSuGiaoDich() {
        try {
            String loaiLoc = (String) view.getCboLoaiLocLichSu().getSelectedItem();

            if ("Theo tên KH".equals(loaiLoc)) { // Đổi thành "Theo tên KH"
                String tenKH = view.getTxtTenKHLocLichSu().getText().trim();
                if (tenKH.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Vui lòng nhập tên khách hàng!");
                    return;
                }

                loadLichSuGiaoDichTheoTen(tenKH);
            } else {
                loadLichSuGiaoDich(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi lọc dữ liệu: " + ex.getMessage());
        }
    }
// Thêm phương thức load theo tên khách hàng

    private void loadLichSuGiaoDichTheoTen(String tenKhachHang) {
        try {
            LichSuGiaoDichTraTruocService service = new LichSuGiaoDichTraTruocService();
            List<LichSuGiaoDichTraTruoc> danhSach = service.getLichSuByTenKhachHang(tenKhachHang);

            currentLichSuGiaoDich = danhSach;
            hienThiLichSuGiaoDich(danhSach);

            if (danhSach.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy lịch sử giao dịch cho khách hàng: " + tenKhachHang);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tải lịch sử giao dịch: " + ex.getMessage());
        }
    }
// Thêm phương thức load lịch sử giao dịch

    private void loadLichSuGiaoDich(Integer maKhachHang) {
        try {
            LichSuGiaoDichTraTruocService service = new LichSuGiaoDichTraTruocService();
            List<LichSuGiaoDichTraTruoc> danhSach;

            if (maKhachHang != null) {
                danhSach = service.getLichSuByMaKhachHang(maKhachHang);
            } else {
                danhSach = service.getAllLichSu();
            }

            currentLichSuGiaoDich = danhSach;
            hienThiLichSuGiaoDich(danhSach);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tải lịch sử giao dịch: " + ex.getMessage());
        }
    }

// Thêm phương thức hiển thị lịch sử giao dịch
    private void hienThiLichSuGiaoDich(List<LichSuGiaoDichTraTruoc> danhSach) {
        DefaultTableModel model = (DefaultTableModel) view.getTblLichSuGiaoDich().getModel();
        model.setRowCount(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (LichSuGiaoDichTraTruoc ls : danhSach) {
            String ngayGiaoDichStr = ls.getNgayGiaoDich() != null
                    ? dateFormat.format(Timestamp.valueOf(ls.getNgayGiaoDich())) : "";

            String maHoaDonStr = ls.getMaHoaDon() != null ? ls.getMaHoaDon().toString() : "";
            String soHoaDonStr = ls.getSoHoaDon() != null ? ls.getSoHoaDon() : maHoaDonStr;

            model.addRow(new Object[]{
                ls.getMaLichSu(),
                ls.getMaKhachHang(),
                ls.getTenKhachHang(),
                soHoaDonStr,
                ngayGiaoDichStr,
                ls.getSoTienTang() != null ? ls.getSoTienTang().doubleValue() : 0,
                ls.getSoTienGiam() != null ? ls.getSoTienGiam().doubleValue() : 0,
                ls.getTongTien() != null ? ls.getTongTien().doubleValue() : 0,
                ls.getTienPhaiTra() != null ? ls.getTienPhaiTra().doubleValue() : 0,
                ls.getGhiChu() != null ? ls.getGhiChu() : ""
            });
        }
    }

    private void loadDanhSachNam() {
        try {
            List<Integer> years = service.getDanhSachNam();
            view.getCboNam().removeAllItems();

            if (years.isEmpty()) {
                // Nếu không có năm nào, thêm năm hiện tại
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                view.getCboNam().addItem(currentYear);
            } else {
                for (Integer year : years) {
                    view.getCboNam().addItem(year);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void thongKe() {
        try {
            Date fromDate = view.getDateFrom();
            Date toDate = view.getDateTo();
            currentYear = (Integer) view.getCboNam().getSelectedItem();

            if (fromDate == null || toDate == null) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!");
                return;
            }

            // Lưu trữ ngày hiện tại
            currentFromDate = fromDate;
            currentToDate = toDate;

            if (fromDate.after(toDate)) {
                JOptionPane.showMessageDialog(view, "Ngày bắt đầu phải trước ngày kết thúc!");
                return;
            }

            // Thực hiện các thống kê
            thucHienThongKe(fromDate, toDate, currentYear);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thực hiện thống kê: " + ex.getMessage());
        }
    }

    private void thucHienThongKe(Date fromDate, Date toDate, int year) {
        try {
            // Thống kê tổng quan
            currentTongQuan = service.thongKeTongQuan(fromDate, toDate);
            hienThiTongQuan(currentTongQuan, fromDate, toDate);

            // Thống kê khách hàng
            currentKhachHang = service.thongKeKhachHangNhieuDichVu(fromDate, toDate, 10);
            hienThiKhachHang(currentKhachHang);

            // Thống kê doanh thu
            currentDoanhThu = service.thongKeDoanhThuTheoThang(year);
            hienThiDoanhThu(currentDoanhThu);

            // Thống kê dịch vụ
            currentDichVu = service.thongKeDichVuBanChay(fromDate, toDate, 10);
            hienThiDichVu(currentDichVu);

            // THỐNG KÊ MỚI: Hóa đơn
            currentHoaDon = service.thongKeHoaDonTheoThoiGian(fromDate, toDate);
            hienThiHoaDon(currentHoaDon);

            // Tự động load lịch sử giao dịch
            loadLichSuGiaoDich(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi thực hiện thống kê: " + ex.getMessage());
        }
    }

    // Thêm phương thức hiển thị hóa đơn
    private void hienThiHoaDon(List<Map<String, Object>> hoaDonList) {
        DefaultTableModel model = (DefaultTableModel) view.getTblHoaDon().getModel();
        model.setRowCount(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Map<String, Object> hd : hoaDonList) {
            Timestamp ngayLap = (Timestamp) hd.get("NgayLap");
            String ngayLapStr = ngayLap != null ? dateFormat.format(ngayLap) : "";

            model.addRow(new Object[]{
                hd.get("MaHoaDon"),
                ngayLapStr,
                hd.get("TenKhachHang"),
                hd.get("TenNhanVien"),
                String.format("%,.0f VND", hd.get("TongTien")),
                String.format("%,d", hd.get("SoDichVu")),
                hd.get("GhiChu") != null ? hd.get("GhiChu") : ""
            });
        }
    }

// Thêm phương thức xem chi tiết hóa đơn (có thể gọi từ sự kiện double-click)
    private void xemChiTietHoaDon(int maHoaDon) {
        try {
            List<Map<String, Object>> chiTiet = service.thongKeChiTietHoaDon(maHoaDon);

            if (chiTiet.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Hóa đơn không có chi tiết!");
                return;
            }

            // Tạo dialog hiển thị chi tiết
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(view), "Chi tiết hóa đơn #" + maHoaDon, true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(800, 400);
            dialog.setLocationRelativeTo(view);

            // Tạo table chi tiết
            String[] cols = {"STT", "Tên dịch vụ", "Loại DV", "Nhân viên thực hiện", "Số lượng", "Đơn giá", "Thành tiền"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);
            JTable tblChiTiet = new JTable(model);

            int stt = 1;
            double tongTien = 0;
            for (Map<String, Object> ct : chiTiet) {
                double thanhTien = ((Number) ct.get("ThanhTien")).doubleValue();
                tongTien += thanhTien;

                model.addRow(new Object[]{
                    stt++,
                    ct.get("TenDichVu"),
                    ct.get("TenLoaiDV"),
                    ct.get("TenNhanVienThucHien"),
                    String.format("%,d", ct.get("SoLuong")),
                    String.format("%,.0f VND", ct.get("DonGia")),
                    String.format("%,.0f VND", thanhTien)
                });
            }

            // Thêm dòng tổng
            model.addRow(new Object[]{
                "", "TỔNG CỘNG", "", "", "", "",
                String.format("%,.0f VND", tongTien)
            });

            JScrollPane scrollPane = new JScrollPane(tblChiTiet);
            dialog.add(scrollPane, BorderLayout.CENTER);

            // Panel nút
            JPanel pnButton = new JPanel();
            JButton btnClose = new JButton("Đóng");
            btnClose.addActionListener(e -> dialog.dispose());
            pnButton.add(btnClose);
            dialog.add(pnButton, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi lấy chi tiết hóa đơn: " + ex.getMessage());
        }
    }

    private void hienThiTongQuan(Map<String, Object> tongQuan, Date fromDate, Date toDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("THỐNG KÊ TỔNG QUAN\n");
        sb.append("Khoảng thời gian: ").append(dateFormat.format(fromDate))
                .append(" - ").append(dateFormat.format(toDate)).append("\n\n");

        int tongHoaDon = ((Number) tongQuan.getOrDefault("TongHoaDon", 0)).intValue();
        double doanhThuHoaDon = ((Number) tongQuan.getOrDefault("DoanhThuHoaDon", 0.0)).doubleValue();
        double tienTraTruoc = ((Number) tongQuan.getOrDefault("TienTraTruoc", 0.0)).doubleValue();
        double tongDoanhThuThucTe = ((Number) tongQuan.getOrDefault("TongDoanhThuThucTe", 0.0)).doubleValue();
        int tongKhachHang = ((Number) tongQuan.getOrDefault("TongKhachHang", 0)).intValue();
        double donGiaTrungBinh = ((Number) tongQuan.getOrDefault("DonGiaTrungBinh", 0.0)).doubleValue();

        sb.append("┌────────────────────────────────────────────┐\n");
        sb.append(String.format("│ %-34s %10s │\n", "Tổng số hóa đơn:", String.format("%,d", tongHoaDon)));
        sb.append(String.format("│ %-34s %10s │\n", "Doanh thu từ hóa đơn:", String.format("%,.0f VND", doanhThuHoaDon)));
        sb.append(String.format("│ %-34s %10s │\n", "Tiền nạp trả trước:", String.format("%,.0f VND", tienTraTruoc)));
        sb.append(String.format("│ %-34s %10s │\n", "Tổng doanh thu thực tế:", String.format("%,.0f VND", tongDoanhThuThucTe)));
        sb.append(String.format("│ %-34s %10s │\n", "Tổng khách hàng:", String.format("%,d", tongKhachHang)));
        sb.append(String.format("│ %-34s %10s │\n", "Đơn giá trung bình:", String.format("%,.0f VND", donGiaTrungBinh)));
        sb.append("└────────────────────────────────────────────┘\n");

        view.getTxtTongQuan().setText(sb.toString());
    }

    private void hienThiKhachHang(List<Map<String, Object>> khachHangList) {
        DefaultTableModel model = (DefaultTableModel) view.getTblKhachHang().getModel();
        model.setRowCount(0);

        int stt = 1;
        for (Map<String, Object> kh : khachHangList) {
            model.addRow(new Object[]{
                stt++,
                kh.get("MaKhachHang"),
                kh.get("HoTen"),
                kh.get("SoDienThoai"),
                kh.get("LoaiKhach"),
                String.format("%,d", kh.get("SoDichVuDaDung")),
                String.format("%,.0f VND", kh.get("TongChiTieu"))
            });
        }
    }

    private void hienThiDoanhThu(List<Map<String, Object>> doanhThuList) {
        DefaultTableModel model = (DefaultTableModel) view.getTblDoanhThu().getModel();
        model.setRowCount(0);

        for (Map<String, Object> dt : doanhThuList) {
            double doanhThu = ((Number) dt.get("DoanhThu")).doubleValue();
            int soHoaDon = ((Number) dt.get("SoHoaDon")).intValue();
            double trungBinh = soHoaDon > 0 ? doanhThu / soHoaDon : 0;

            model.addRow(new Object[]{
                "Tháng " + dt.get("Thang"),
                String.format("%,.0f VND", doanhThu),
                String.format("%,d", soHoaDon),
                String.format("%,.0f VND", trungBinh)
            });
        }
    }

    private void hienThiDichVu(List<Map<String, Object>> dichVuList) {
        DefaultTableModel model = (DefaultTableModel) view.getTblDichVu().getModel();
        model.setRowCount(0);

        int stt = 1;
        for (Map<String, Object> dv : dichVuList) {
            model.addRow(new Object[]{
                stt++,
                dv.get("MaDichVu"),
                dv.get("TenDichVu"),
                dv.get("TenLoaiDV"),
                String.format("%,.0f VND", dv.get("Gia")),
                String.format("%,d", dv.get("SoLuongBan")),
                String.format("%,.0f VND", dv.get("DoanhThu"))
            });
        }
    }

    private void xuatExcel() {
        // Kiểm tra xem có dữ liệu để xuất không
        if (currentTongQuan == null || currentKhachHang == null
                || currentDoanhThu == null || currentDichVu == null
                || currentLichSuGiaoDich == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng thực hiện thống kê trước khi xuất Excel!");
            return;
        }

        try {
            // Tạo đường dẫn động đến thư mục resources/thongke
            String resourcesPath = DataConnection.getThongkePath();

            // Tạo thư mục nếu chưa tồn tại
            File resourcesDir = new File(resourcesPath);
            if (!resourcesDir.exists()) {
                resourcesDir.mkdirs();
            }

            // Tạo tên file với timestamp
            String fileName = "BaoCaoThongKe_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".xlsx";
            File file = new File(resourcesDir, fileName);

            Workbook workbook = new XSSFWorkbook();
            createExcelSheets(workbook);

            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            // Mở file Excel
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

            JOptionPane.showMessageDialog(view,
                    "Đã xuất báo cáo Excel thành công!\n"
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi xuất báo cáo Excel: " + ex.getMessage());
        }
    }

    private void createExcelSheets(Workbook workbook) {
        // Tạo các style
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);

        // Sheet tổng quan
        createTongQuanSheet(workbook, headerStyle, titleStyle, currencyStyle);

        // Sheet khách hàng
        createKhachHangSheet(workbook, headerStyle, titleStyle, currencyStyle);

        // Sheet doanh thu
        createDoanhThuSheet(workbook, headerStyle, titleStyle, currencyStyle);

        // Sheet dịch vụ
        createDichVuSheet(workbook, headerStyle, titleStyle, currencyStyle);

        // SHEET MỚI: Hóa đơn
        createHoaDonSheet(workbook, headerStyle, titleStyle, currencyStyle);

        createLichSuGiaoDichSheet(workbook, headerStyle, titleStyle, currencyStyle);
    }

    // Thêm phương thức tạo sheet Excel cho lịch sử giao dịch
    private void createLichSuGiaoDichSheet(Workbook workbook, CellStyle headerStyle, CellStyle titleStyle, CellStyle currencyStyle) {
        if (currentLichSuGiaoDich == null || currentLichSuGiaoDich.isEmpty()) {
            return;
        }

        Sheet sheet = workbook.createSheet("LS Giao dịch trả trước");

        // Tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("LỊCH SỬ GIAO DỊCH TRẢ TRƯỚC");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

        // Header
        Row headerRow = sheet.createRow(2);
        String[] headers = {"Mã LS", "Mã KH", "Tên KH", "Mã HĐ", "Ngày GD", "Số tiền tăng (VND)", "Số tiền giảm (VND)", "Tổng tiền (VND)", "Tiền phải trả (VND)", "Ghi chú"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dữ liệu
        int rowNum = 3;
        SimpleDateFormat dateFormatExcel = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (LichSuGiaoDichTraTruoc ls : currentLichSuGiaoDich) {
            Row row = sheet.createRow(rowNum++);

            String ngayGiaoDichStr = ls.getNgayGiaoDich() != null
                    ? dateFormatExcel.format(Timestamp.valueOf(ls.getNgayGiaoDich())) : "";
            String maHoaDonStr = ls.getMaHoaDon() != null ? ls.getMaHoaDon().toString() : "";
            String soHoaDonStr = ls.getSoHoaDon() != null ? ls.getSoHoaDon() : maHoaDonStr;

            row.createCell(0).setCellValue(ls.getMaLichSu() != null ? ls.getMaLichSu() : 0);
            row.createCell(1).setCellValue(ls.getMaKhachHang());
            row.createCell(2).setCellValue(ls.getTenKhachHang() != null ? ls.getTenKhachHang() : "");
            row.createCell(3).setCellValue(soHoaDonStr);
            row.createCell(4).setCellValue(ngayGiaoDichStr);

            // Các cột số tiền
            Cell[] currencyCells = new Cell[4];
            currencyCells[0] = row.createCell(5);
            currencyCells[1] = row.createCell(6);
            currencyCells[2] = row.createCell(7);
            currencyCells[3] = row.createCell(8);

            BigDecimal[] amounts = {
                ls.getSoTienTang(),
                ls.getSoTienGiam(),
                ls.getTongTien(),
                ls.getTienPhaiTra()
            };

            for (int i = 0; i < currencyCells.length; i++) {
                double value = amounts[i] != null ? amounts[i].doubleValue() : 0;
                currencyCells[i].setCellValue(value);
                currencyCells[i].setCellStyle(currencyStyle);
            }

            row.createCell(9).setCellValue(ls.getGhiChu() != null ? ls.getGhiChu() : "");
        }

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
// Thêm phương thức tạo sheet hóa đơn

    private void createHoaDonSheet(Workbook workbook, CellStyle headerStyle, CellStyle titleStyle, CellStyle currencyStyle) {
        Sheet sheet = workbook.createSheet("Hóa đơn");

        // Tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DANH SÁCH HÓA ĐƠN");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        // Thời gian
        Row timeRow = sheet.createRow(1);
        timeRow.createCell(0).setCellValue("Thời gian: "
                + dateFormat.format(currentFromDate) + " - " + dateFormat.format(currentToDate));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));

        // Header
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Mã HĐ", "Ngày lập", "Khách hàng", "Nhân viên", "Tổng tiền (VND)", "Số DV", "Ghi chú"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dữ liệu
        int rowNum = 4;
        SimpleDateFormat dateFormatExcel = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Map<String, Object> hd : currentHoaDon) {
            Row row = sheet.createRow(rowNum++);
            Timestamp ngayLap = (Timestamp) hd.get("NgayLap");
            String ngayLapStr = ngayLap != null ? dateFormatExcel.format(ngayLap) : "";

            row.createCell(0).setCellValue(((Number) hd.get("MaHoaDon")).intValue());
            row.createCell(1).setCellValue(ngayLapStr);
            row.createCell(2).setCellValue((String) hd.get("TenKhachHang"));
            row.createCell(3).setCellValue((String) hd.get("TenNhanVien"));

            Cell tongTienCell = row.createCell(4);
            tongTienCell.setCellValue(((Number) hd.get("TongTien")).doubleValue());
            tongTienCell.setCellStyle(currencyStyle);

            row.createCell(5).setCellValue(((Number) hd.get("SoDichVu")).intValue());
            row.createCell(6).setCellValue(hd.get("GhiChu") != null ? (String) hd.get("GhiChu") : "");
        }

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createTongQuanSheet(Workbook workbook, CellStyle headerStyle, CellStyle titleStyle, CellStyle currencyStyle) {
        Sheet sheet = workbook.createSheet("Tổng quan");

        // Tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("BÁO CÁO THỐNG KÊ TỔNG QUAN");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        // Thời gian
        Row timeRow = sheet.createRow(1);
        timeRow.createCell(0).setCellValue("Thời gian: "
                + dateFormat.format(currentFromDate) + " - " + dateFormat.format(currentToDate));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));

        // Header
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Chỉ tiêu", "Giá trị", "Đơn vị"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dữ liệu
        int rowNum = 4;
        String[][] data = {
            {"Tổng số hóa đơn", String.valueOf(currentTongQuan.get("TongHoaDon")), "hóa đơn"},
            {"Doanh thu từ hóa đơn", String.format("%,.0f", currentTongQuan.get("DoanhThuHoaDon")), "VND"},
            {"Tiền nạp trả trước", String.format("%,.0f", currentTongQuan.get("TienTraTruoc")), "VND"},
            {"Tổng doanh thu thực tế", String.format("%,.0f", currentTongQuan.get("TongDoanhThuThucTe")), "VND"},
            {"Tổng khách hàng", String.valueOf(currentTongQuan.get("TongKhachHang")), "khách hàng"},
            {"Đơn giá trung bình", String.format("%,.0f", currentTongQuan.get("DonGiaTrungBinh")), "VND"}
        };

        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowData[0]);
            row.createCell(1).setCellValue(rowData[1]);
            row.createCell(2).setCellValue(rowData[2]);
        }

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createKhachHangSheet(Workbook workbook, CellStyle headerStyle, CellStyle titleStyle, CellStyle currencyStyle) {
        Sheet sheet = workbook.createSheet("Khách hàng");

        // Tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("TOP KHÁCH HÀNG SỬ DỤNG NHIỀU DỊCH VỤ NHẤT");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        // Header
        Row headerRow = sheet.createRow(2);
        String[] headers = {"STT", "Mã KH", "Họ tên", "Số điện thoại", "Loại KH", "Số DV đã dùng", "Tổng chi tiêu (VND)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dữ liệu
        int rowNum = 3;
        int stt = 1;
        for (Map<String, Object> kh : currentKhachHang) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(((Number) kh.get("MaKhachHang")).intValue());
            row.createCell(2).setCellValue((String) kh.get("HoTen"));
            row.createCell(3).setCellValue((String) kh.get("SoDienThoai"));
            row.createCell(4).setCellValue((String) kh.get("LoaiKhach"));
            row.createCell(5).setCellValue(((Number) kh.get("SoDichVuDaDung")).intValue());

            Cell doanhThuCell = row.createCell(6);
            doanhThuCell.setCellValue(((Number) kh.get("TongChiTieu")).doubleValue());
            doanhThuCell.setCellStyle(currencyStyle);
        }

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createDoanhThuSheet(Workbook workbook, CellStyle headerStyle, CellStyle titleStyle, CellStyle currencyStyle) {
        Sheet sheet = workbook.createSheet("Doanh thu");

        // Tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DOANH THU THEO THÁNG NĂM " + currentYear);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        // Header
        Row headerRow = sheet.createRow(2);
        String[] headers = {"Tháng", "Doanh thu (VND)", "Số hóa đơn", "Doanh thu trung bình (VND)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dữ liệu
        int rowNum = 3;
        double tongDoanhThu = 0;
        int tongHoaDon = 0;

        for (Map<String, Object> dt : currentDoanhThu) {
            Row row = sheet.createRow(rowNum++);
            double doanhThu = ((Number) dt.get("DoanhThu")).doubleValue();
            int soHoaDon = ((Number) dt.get("SoHoaDon")).intValue();
            double trungBinh = soHoaDon > 0 ? doanhThu / soHoaDon : 0;

            tongDoanhThu += doanhThu;
            tongHoaDon += soHoaDon;

            row.createCell(0).setCellValue("Tháng " + dt.get("Thang"));

            Cell doanhThuCell = row.createCell(1);
            doanhThuCell.setCellValue(doanhThu);
            doanhThuCell.setCellStyle(currencyStyle);

            row.createCell(2).setCellValue(soHoaDon);

            Cell trungBinhCell = row.createCell(3);
            trungBinhCell.setCellValue(trungBinh);
            trungBinhCell.setCellStyle(currencyStyle);
        }

        // Dòng tổng
        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(0).setCellValue("TỔNG CỘNG");

        Cell totalDoanhThuCell = totalRow.createCell(1);
        totalDoanhThuCell.setCellValue(tongDoanhThu);
        totalDoanhThuCell.setCellStyle(currencyStyle);

        totalRow.createCell(2).setCellValue(tongHoaDon);

        Cell totalTrungBinhCell = totalRow.createCell(3);
        totalTrungBinhCell.setCellValue(tongHoaDon > 0 ? tongDoanhThu / tongHoaDon : 0);
        totalTrungBinhCell.setCellStyle(currencyStyle);

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createDichVuSheet(Workbook workbook, CellStyle headerStyle, CellStyle titleStyle, CellStyle currencyStyle) {
        Sheet sheet = workbook.createSheet("Dịch vụ");

        // Tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("TOP DỊCH VỤ BÁN CHẠY");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        // Header
        Row headerRow = sheet.createRow(2);
        String[] headers = {"STT", "Mã DV", "Tên dịch vụ", "Loại DV", "Đơn giá (VND)", "Số lượng bán", "Doanh thu (VND)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dữ liệu
        int rowNum = 3;
        int stt = 1;
        double tongDoanhThu = 0;

        for (Map<String, Object> dv : currentDichVu) {
            Row row = sheet.createRow(rowNum++);
            double doanhThu = ((Number) dv.get("DoanhThu")).doubleValue();
            tongDoanhThu += doanhThu;

            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(((Number) dv.get("MaDichVu")).intValue());
            row.createCell(2).setCellValue((String) dv.get("TenDichVu"));
            row.createCell(3).setCellValue((String) dv.get("TenLoaiDV"));

            Cell giaCell = row.createCell(4);
            giaCell.setCellValue(((Number) dv.get("Gia")).doubleValue());
            giaCell.setCellStyle(currencyStyle);

            row.createCell(5).setCellValue(((Number) dv.get("SoLuongBan")).intValue());

            Cell doanhThuCell = row.createCell(6);
            doanhThuCell.setCellValue(doanhThu);
            doanhThuCell.setCellStyle(currencyStyle);
        }

        // Dòng tổng
        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(5).setCellValue("TỔNG CỘNG");
        Cell totalDoanhThuCell = totalRow.createCell(6);
        totalDoanhThuCell.setCellValue(tongDoanhThu);
        totalDoanhThuCell.setCellStyle(currencyStyle);

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
