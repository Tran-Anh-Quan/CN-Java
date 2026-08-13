package vn.edu.eaut.lab5.ui.worker;

import vn.edu.eaut.lab5.bus.HoaDonBUS;
import vn.edu.eaut.lab5.model.HoaDon;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class HoaDonSearchWorker extends SwingWorker<List<HoaDon>, Void> {
    private final HoaDonBUS hoaDonBUS;
    private final LocalDate tuNgay;
    private final LocalDate denNgay;
    private final int maKh;
    private final DefaultTableModel tableModel;
    private final Consumer<Exception> onError;

    public HoaDonSearchWorker(HoaDonBUS hoaDonBUS, LocalDate tuNgay, LocalDate denNgay, int maKh,
                              DefaultTableModel tableModel, Consumer<Exception> onError) {
        this.hoaDonBUS = hoaDonBUS;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.maKh = maKh;
        this.tableModel = tableModel;
        this.onError = onError;
    }

    @Override
    protected List<HoaDon> doInBackground() throws Exception {
        return hoaDonBUS.findByDateAndCustomer(tuNgay, denNgay, maKh);
    }

    @Override
    protected void done() {
        try {
            List<HoaDon> list = get();
            tableModel.setRowCount(0);
            for (HoaDon hd : list) {
                tableModel.addRow(new Object[]{
                        hd.getMaHd(),
                        hd.getNgayLap(),
                        hd.getMaKh(),
                        hd.getTenKh(),
                        hd.getTongTien()
                });
            }
        } catch (Exception e) {
            onError.accept(e);
        }
    }
}
