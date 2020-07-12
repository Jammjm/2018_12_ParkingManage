package parkinglot;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class ExitCarData {

	private JFrame frame;
	public JTable exitCarData_table;

	/**
	 * Launch the application.
	 * 
	 * 
	 * 출차내역 화면을 표시하며 엑셀로 내보낼 수 있다.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExitCarData window = new ExitCarData();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ExitCarData() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 684, 519);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 678, 475);
		frame.getContentPane().add(panel);

		panel.setLayout(null);
		// ParkingLot p = new ParkingLot();
		exitCarData_table = new JTable(ParkingLot.dtm);
		exitCarData_table.setFont(new Font("나눔스퀘어라운드 Light", Font.PLAIN, 13));
		JScrollPane ExitCarData_Scroll = new JScrollPane(exitCarData_table);
		ExitCarData_Scroll.setBounds(0, 23, 678, 408);
		panel.add(ExitCarData_Scroll);

		JButton ExitCarFile_btn = new JButton("\uB0B4\uBCF4\uB0B4\uAE30");
		ExitCarFile_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		ExitCarFile_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = new File("C:\\Users\\HP_Omen\\Desktop\\data.xlsx");
				try {
					toExcel(exitCarData_table);
					JOptionPane.showMessageDialog(null, "내보내기 성공");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null,
							"FileNotFoundExeption 발생\n\n 지정된 경로에 엑셀서식파일 data.xls가 있는지\n현재 출차엑셀파일이 열려있는건 아닌지 확인하세요 ",
							"경고", JOptionPane.WARNING_MESSAGE);
					JOptionPane.showMessageDialog(null, "내보내기 실패");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,
							"알수없는 오류 발생. 관리자에게 문의하세요 ",
							"경고", JOptionPane.WARNING_MESSAGE);
					JOptionPane.showMessageDialog(null, "내보내기 실패");
					e1.printStackTrace();
				}

			}
		});
		ExitCarFile_btn.setBounds(569, 444, 97, 25);
		panel.add(ExitCarFile_btn);

		JButton ExitCarDel_btn = new JButton("\uC0AD\uC81C");
		ExitCarDel_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		ExitCarDel_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (exitCarData_table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "선택된 항목이 없습니다", "경고", JOptionPane.WARNING_MESSAGE);
				} else {
					ParkingLot.dtm.removeRow(exitCarData_table.getSelectedRow());
				}
			}
		});
		ExitCarDel_btn.setBounds(459, 444, 97, 25);
		panel.add(ExitCarDel_btn);

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setFont(new Font("나눔스퀘어라운드 Light", Font.PLAIN, 13));
		lblNewLabel.setText("근무자: " + ParkingLot.EmployeeName);
		lblNewLabel.setBounds(0, 3, 97, 16);
		panel.add(lblNewLabel);

	}

	public void toExcel(JTable table) throws Exception {

		Calendar cal = Calendar.getInstance();
		int year, mon, date, daynum;
		year = cal.get(Calendar.YEAR);
		mon = cal.get(Calendar.MONTH) + 1;
		date = cal.get(Calendar.DAY_OF_MONTH);
		daynum = cal.get(Calendar.DAY_OF_WEEK);
		String day = null;
		switch (daynum) {
		case 1:
			day = "일요일";
			break;
		case 2:
			day = "월요일";
			break;
		case 3:
			day = "화요일";
			break;
		case 4:
			day = "수요일";
			break;
		case 5:
			day = "목요일";
			break;
		case 6:
			day = "금요일";
			break;
		case 7:
			day = "토요일";
			break;
		}
		System.out.println(daynum);
		FileInputStream fis = new FileInputStream("C:\\Users\\HP_Omen\\Desktop\\data.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		String DataName = "a";
		HSSFSheet sheet1 = workbook.getSheetAt(0);
		workbook.setSheetName(0, "출차관리");
		HSSFRow row;
		String fileName = year + "." + mon + "." + date + " 입출차 내역";
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		row = sheet1.getRow(9);
		row.createCell(0).setCellValue(
				year + "년 " + mon + "월 " + date + "일 " + "(" + day + ")   " + "근무자: " + ParkingLot.EmployeeName);

		CellStyle doubleStyle = workbook.createCellStyle();
		doubleStyle.setBorderBottom(BorderStyle.THIN);
		doubleStyle.setBorderTop(BorderStyle.THIN);
		doubleStyle.setBorderLeft(BorderStyle.THIN);
		doubleStyle.setBorderRight(BorderStyle.DOUBLE);
		doubleStyle.setAlignment(HorizontalAlignment.CENTER);
		doubleStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

		TableModel model = table.getModel();

		for (int i = 0; i < model.getRowCount(); i++) {

			String check = (String) model.getValueAt(i, 0);
			row = sheet1.getRow(i + 12);
			if (check.contains("무료")) {
				row.createCell(1).setCellStyle(style);
				row.getCell(1).setCellValue((String) table.getValueAt(i, 2));
				row.createCell(2).setCellStyle(style);
				row.getCell(2).setCellValue((String) table.getValueAt(i, 1));
				row.createCell(3).setCellStyle(style);
				row.getCell(3).setCellValue((String) table.getValueAt(i, 3));
				row.createCell(4).setCellStyle(style);
				row.getCell(4).setCellStyle(doubleStyle);
				row.getCell(4).setCellValue((String) table.getValueAt(i, 5));
			} else if (check.contains("유료")) {
				row.createCell(5).setCellStyle(style);
				row.getCell(5).setCellValue((String) table.getValueAt(i, 2));
				row.createCell(6).setCellStyle(style);
				row.getCell(6).setCellValue((String) table.getValueAt(i, 1));
				row.createCell(7).setCellStyle(style);
				row.getCell(7).setCellValue((String) table.getValueAt(i, 3));
				row.createCell(8).setCellStyle(style);
				String Moneystr = (String) table.getValueAt(i, 4);
				int money = Integer.parseInt(Moneystr.substring(0, Moneystr.length() - 1));
				row.getCell(8).setCellValue(money);
			}

		}

		workbook.setForceFormulaRecalculation(true);

		FileOutputStream fos = new FileOutputStream("C:\\Users\\HP_Omen\\Desktop/" + fileName + ".xls");
		workbook.write(fos);
		fos.close();

	}// METHOD END

}
