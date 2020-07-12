package parkinglot;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.SystemColor;
import javax.swing.JComboBox;

// 요금할인 
//주차번호 7자리 수정
class Name extends JFrame {
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
}

// 무료주차 테이블
class freeCar_Table extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComponent component;

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public freeCar_Table(DefaultTableModel dtm) {
		super(dtm);
	}

	public int checkTime(int i) {
		String elapseTime = (String) getValueAt(i, 2);
		int timeidx = elapseTime.indexOf("분");
		int time = Integer.parseInt(elapseTime.substring(0, timeidx));
		System.out.println(time);
		return time;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {

		component = (JComponent) super.prepareRenderer(renderer, row, col);

		/*
		 * for (int i = 0; i < row; i++) { if (checkTime(i) >= 1) { if(i == row) {
		 * component.setBackground(Color.red); }else {
		 * component.setBackground(Color.WHITE); } }
		 * 
		 * }
		 */
		return component;
	}
}

public class ParkingLot extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel ParkingLot;
	private JPanel NonFree_Panel;
	private JPanel Free_Panel;
	private JPanel exitCarFeeIs_label;
	private JTextField NewCar_TextField;
	public JLabel clockNow_label;
	private JTable NonfreeCar_Table;
	// private JTable freeCar_Table;
	private int hour, min, second;
	private JLabel carTimeALert_label;
	//private int alertLabelFlag = 0;
	private int warningTime = 20;
	private boolean flag = true;
	private JTextField exitCarNum_txtfld;
	private JTextField exitCarEnterTimeHour_txtfld;
	private JTextField exitCarEnterTimeMin_txtfld;
	private JTextField exitCarExitTimeHour_txtfld;
	private JTextField exitCarExitTimeMin_txtfld;
	private JLabel exitCarNum_label, exitCarEnterTime_label, exitCarExitTime_label, exitCarType_label, exitCarFee_label,
			exitCarElapseTime_label, exitCarEnterTimeHour_label, exitCarExitTimeHour_label, exitCarExitTimeMin_label,
			exitCarEnterTimeMin_label;
	private JLabel exitCarType_cbobx;
	private JLabel exitCarElapseTimeShowMin_label;
	private JLabel exitCarFeeShow_label;
	private JButton NonfreeCarDelete_btn;
	private JButton exitCarEdit_btn;
	private JLabel ExitCarElapseTimeShow_label;
	private JButton btnNewButton;
	private JButton ExitCarList_btn;
	public static String ExitCarDatahead[] = { "구분", "차량번호", "입차시간", "출차시간", "요금", "사유" };
	public static String ExitCarDatadata[][] = {

	};
	private String reason;
	private String entHr, entMn, extHr, extMn;
	public static DefaultTableModel dtm = new DefaultTableModel(ExitCarDatadata, ExitCarDatahead);
	private JPanel btnpanel;
	private JButton btnNewButton_1;
	public static String EmployeeName;
	private JButton exitCarDataModify_btn;

	// public Calendar cal = Calendar.getInstance();
	// public static JTable freeCar_Table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ParkingLot frame = new ParkingLot();
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setLocationRelativeTo(null);
					frame.setJMenuBar(Menu());
					frame.setTitle("주차관리시스템");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static JMenuBar Menu() {
		JMenuBar menu = new JMenuBar();
		return menu;
	}

	// time method
	//왼쪽 상단 현재 시간을 시 분 초 단위로 표시
	//1초마다 refresh
	public void clock() {
		Thread clock = new Thread() {
			public void run() {

				try {
					// setElapseTime();
					while (true) {
						Calendar cal = Calendar.getInstance();
						min = cal.get(Calendar.MINUTE);
						hour = cal.get(Calendar.HOUR_OF_DAY);
						second = cal.get(Calendar.SECOND);
						clockNow_label.setText("현재시간: " + hour + "시" + min + "분 " + second + "초");
						sleep(1000);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		clock.start();
	}

		
	//경과 시간 계산해서 표시함
	public void setElapseTime(JTable freeCar_Table) {
		Thread elapseTime = new Thread() {
			public void run() {
				try {
					while (flag) {
						int freeCarRowNum = freeCar_Table.getRowCount();
						int nonFreeCarRowNum = NonfreeCar_Table.getRowCount();
						if (freeCarRowNum != 0) {
							
							//무로주차대상 테이블에 경과시간을 표시하며 무료 주차시간인 20분 경과할 경우 경고 메세지 깜빡이며 출력
							for (int i = 0; i < freeCarRowNum; i++) {
								int elapseTime = CalculateElapseTime(freeCar_Table, i);
								freeCar_Table.setValueAt(new String(elapseTime + "분"), i, 2);
								if (elapseTime >= warningTime) {
									carTimeALert_label.setText("영수증 발급대상차량이 존재합니다");

									// carTimeALert_label.setFont(new Font("나눔스퀘어라운드OTF ExtraBold", Font.PLAIN,
									// 18));
									// alertLabelFlag = 1;
								}
							}
						}
						
						// 유료주차대상 테이블에 경과시간을 표시
						if (nonFreeCarRowNum != 0) {
							for (int i = 0; i < nonFreeCarRowNum; i++) {
								int elapseTime = CalculateElapseTime(NonfreeCar_Table, i);
								NonfreeCar_Table.setValueAt(new String(elapseTime + "분"), i, 2);
							}
						}
						sleep(700);
						carTimeALert_label.setText("");
						sleep(300);
					}
				} catch (Exception e) {

				}

			}
		};
		elapseTime.start();
	}

		
	// 경과한 시간을 계산하는 메서드
	public int CalculateElapseTime(JTable table, int i) {

		int idxhr, idxmin;
		String enterTime = (String) table.getValueAt(i, 0);
		idxhr = enterTime.indexOf("시");
		idxmin = enterTime.indexOf("분");
		int enterHour = Integer.parseInt((enterTime.substring(0, idxhr)));
		int enterMin = Integer.parseInt((enterTime.substring(idxhr + 1, idxmin)));
		int elapseTime = (hour - enterHour) * 60 + (min - enterMin);
		if (elapseTime < 0) {
			elapseTime = 0;
		}

		return elapseTime;
	}

	
	//테이블 안에 20분을 경과한 차량이 있는지 확인하는 메서드
	public boolean checkAlertOverCar(JTable freeCar_Table) {
		int rowNum = freeCar_Table.getRowCount();
		int sum = 1;
		for (int i = 0; i < rowNum; i++) {
			String elapseTime = (String) freeCar_Table.getValueAt(i, 2);
			int timeidx = elapseTime.indexOf("분");
			int elapsetime = Integer.parseInt(elapseTime.substring(0, timeidx));
			if (elapsetime >= warningTime) {
				sum *= 0;
				break;
			}
		}
		if (sum == 0)
			return true;
		else
			return false;

	}

	// 30분마다 유료대상차량이 무료대상 테이블에 있는경우 경고 메세지를 띄움
	public void alertOverCar(JTable freeCar_Table) {
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					boolean check = checkAlertOverCar(freeCar_Table);
					if (check) {
						JOptionPane.showMessageDialog(null, "영수증 발급대상차량이 존재합니다", "경고", JOptionPane.WARNING_MESSAGE);
					}
					try {
						sleep(1800000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		};
		thread.start();
	}

	// 동일차량 검증 메소드
	public boolean checkSameCar(JTable freeCar_Table, String Carnum) {

		for (int i = 0; i < freeCar_Table.getRowCount(); i++) {
			if (freeCar_Table.getValueAt(i, 1).equals(Carnum)) {
				return true;
			}
		}
		for (int i = 0; i < NonfreeCar_Table.getRowCount(); i++) {
			if (NonfreeCar_Table.getValueAt(i, 1).equals(Carnum)) {
				return true;
			}
		}

		return false;
	}

	
	// 수정 / 출차 패널안에 버튼이 클릭 되었을 경우 실행이 됨
	// 수정/출차 패널에 관한 설정 메서드
	// 오류가 발생하는경우 일부러 ArithmeticException 발생시켜 1을 리턴
	public int ModifyFeeIsPanel(JTable freeCar_Table) {

		String newCarNum = exitCarNum_txtfld.getText();// 츨치 페널에 출차 차량번호를 가져옴
		String nowCarNum = null;// 테이블에 선택되어 있는 차량번호를 가져옴

		try {
			if (newCarNum.length() < 4) {
				JOptionPane.showMessageDialog(null, "차량번호를 확인해주세요", "경고", JOptionPane.WARNING_MESSAGE);
				int i = 0;
				int j = 10 / i;
			} else if (NonfreeCar_Table.getSelectedRow() != -1) { //유료출차차량이 선택되어 있는경우
				nowCarNum = (String) NonfreeCar_Table.getValueAt(NonfreeCar_Table.getSelectedRow(), 1);
				if (Integer.parseInt(nowCarNum) == Integer.parseInt(newCarNum)) {

				} else if (checkSameCar(freeCar_Table, newCarNum)) {
					JOptionPane.showMessageDialog(null, "동일한 차량이 존재합니다", "경고", JOptionPane.WARNING_MESSAGE);
					int i = 0;
					int j = 10 / i;
				} else {
					NonfreeCar_Table.setValueAt(newCarNum, NonfreeCar_Table.getSelectedRow(), 1);

				}
			} else { // 무료출차차량이 선택되어 있는경우
				nowCarNum = (String) freeCar_Table.getValueAt(freeCar_Table.getSelectedRow(), 1);

				if (Integer.parseInt(nowCarNum) == Integer.parseInt(newCarNum)) {

				} else if (checkSameCar(freeCar_Table, newCarNum)) {
					JOptionPane.showMessageDialog(null, "동일한 차량이 존재합니다", "경고", JOptionPane.WARNING_MESSAGE);
					int i = 0;
					int j = 10 / i;
				} else {
					freeCar_Table.setValueAt(newCarNum, freeCar_Table.getSelectedRow(), 1);

				}
			}

			// carNum = exitCarNum_txtfld.getText();

			
			// 시간검증 
			entHr = exitCarEnterTimeHour_txtfld.getText();
			entMn = exitCarEnterTimeMin_txtfld.getText();
			extHr = exitCarExitTimeHour_txtfld.getText();
			extMn = exitCarExitTimeMin_txtfld.getText();

			if ((Integer.parseInt(entMn)) >= 60 || (Integer.parseInt(extMn)) >= 60 || (Integer.parseInt(entHr)) >= 24
					|| (Integer.parseInt(extHr)) >= 24) {
				JOptionPane.showMessageDialog(null, "시간입력 오류입니다 다시 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
				int i = 0;
				int j = 10 / i;
			}
			int elapseTime = ((Integer.parseInt(extHr) - Integer.parseInt(entHr)) * 60)
					+ ((Integer.parseInt(extMn) - Integer.parseInt(entMn)));
			ExitCarElapseTimeShow_label.setText(elapseTime + "");

			if (elapseTime >= warningTime) {
				exitCarType_cbobx.setText("유료차량");
			} else {
				exitCarType_cbobx.setText("무료차량");
			}
			
			//금액계산
			int moneyCal = (elapseTime / 10) - 1;
			int money = moneyCal * 200;
			if (money <= 0)
				money = 0;
			else if (money >= 3000) {
				money = 3000;
			}

			exitCarFeeShow_label.setText(money + "원");

		} catch (ArithmeticException e) {
			return 1;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "알수없는 오류입니다 다시 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
			return 1;
		}
		return 0;
	}

	//수정/출차 패널 띄우기
	public void AbleExitCarPage() {
		exitCarFeeIs_label.setVisible(true);
		NonFree_Panel.setEnabled(false);
		Free_Panel.setEnabled(false);
		flag = false;

	}
	// 수정 / 출차 패널 없애기
	public void DisableExitCarPage(JTable table) {
		exitCarFeeIs_label.setVisible(false);
		NonFree_Panel.setEnabled(true);
		Free_Panel.setEnabled(true);
		flag = true;
		setElapseTime(table);

	}

	
	// 메인 패널에서 수정/출처 버튼을 클릭할 경우 수정/출차 패널 정보 띄우기
	// ModifyFeeIsPanel 메서드랑 중복되는게 많음...
	public void setExitCarPanel(JTable table, int selectrow, boolean unknownFlag) {
		String Carnum = (String) table.getValueAt(selectrow, 1);
		exitCarNum_txtfld.setText(Carnum);

		// 입차 시
		String enterTime = (String) table.getValueAt(selectrow, 0);

		int timeidx = enterTime.indexOf("시");
		String enterHour = enterTime.substring(0, timeidx);
		exitCarEnterTimeHour_txtfld.setText(enterHour);
		// 입차 분
		int mintimeidx = enterTime.indexOf("분");
		String enterMin = enterTime.substring(timeidx + 1, mintimeidx);
		exitCarEnterTimeMin_txtfld.setText(enterMin);
		// 출차 시
		if (unknownFlag == true) {

		} else {
			exitCarExitTimeHour_txtfld.setText((hour + ""));
			exitCarExitTimeMin_txtfld.setText(min + "");
		}

		int elapseTime = Integer.parseInt(((String) table.getValueAt(selectrow, 2)).substring(0,
				((String) table.getValueAt(selectrow, 2)).indexOf("분")));
		ExitCarElapseTimeShow_label.setText(elapseTime + "");
		if (elapseTime >= warningTime) {
			exitCarType_cbobx.setText("유료차량");
		} else {
			exitCarType_cbobx.setText("무료차량");
		}

		int moneyCal = (int) (elapseTime * (0.1));
		// System.out.println(moneyCal);
		int money = (moneyCal - 1) * 200;
		// System.out.println(money);
		if (money <= 0)
			money = 0;
		else if (money >= 3000) {
			money = 3000;
		}

		exitCarFeeShow_label.setText(money + "원");

	}

	/**
	 * Create the frame.
	 */
	public ParkingLot() {
		setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 878, 640);
		ParkingLot = new JPanel();
		ParkingLot.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(ParkingLot);
		ParkingLot.setLayout(null);
		

		// 무료주차 테이블 생성
		//
		//
		//

		String freeCarHead[] = { "입차시간", "차량번호", "경과시간" };
		String freeCarData[][] = {

		};
		DefaultTableModel freeCar_Model = new DefaultTableModel(freeCarData, freeCarHead);
		freeCar_Table freeCar_Table = new freeCar_Table(freeCar_Model);

		// 유료주차 테이블
		// 무료주차 판넬에 영수증 발급버튼이 이루어질경우에만 옮겨짐
		//
		//
		//

		String NonfreeCarHead[] = { "입차시간", "차량번호", "경과시간" };
		String NonfreeCarData[][] = {

		};
		DefaultTableModel NonfreeCar_Model = new DefaultTableModel(NonfreeCarData, NonfreeCarHead);

		//
		//
		//
		//
		//
		//
		exitCarFeeIs_label = new JPanel();
		exitCarFeeIs_label.setBackground(SystemColor.controlShadow);
		exitCarFeeIs_label.setBounds(172, 104, 511, 363);
		ParkingLot.add(exitCarFeeIs_label);
		exitCarFeeIs_label.setVisible(false);
		exitCarFeeIs_label.setLayout(null);
		
		
		//수정 출차 패널내 취소 버튼 클릭시 패널 비활성화
		JButton exitCarCancel_btn = new JButton("\uCDE8\uC18C");
		exitCarCancel_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisableExitCarPage(freeCar_Table);
			}
		});
		
		
		// 수정 출차 패널 내 라벨들 설정
		exitCarCancel_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		exitCarCancel_btn.setBounds(391, 325, 97, 25);
		exitCarFeeIs_label.add(exitCarCancel_btn);

		exitCarNum_label = new JLabel("\uCC28\uB7C9\uBC88\uD638");
		exitCarNum_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarNum_label.setOpaque(true);
		exitCarNum_label.setBackground(Color.WHITE);
		exitCarNum_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarNum_label.setBounds(89, 29, 81, 36);

		exitCarFeeIs_label.add(exitCarNum_label);

		exitCarNum_txtfld = new JTextField();
		exitCarNum_txtfld.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarNum_txtfld.setBounds(203, 29, 140, 36);
		exitCarNum_txtfld.setDocument(new JTextFieldLimit(4));
		exitCarFeeIs_label.add(exitCarNum_txtfld);
		exitCarNum_txtfld.setColumns(10);

		exitCarEnterTime_label = new JLabel("\uC785\uCC28\uC2DC\uAC04");
		exitCarEnterTime_label.setOpaque(true);
		exitCarEnterTime_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarEnterTime_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarEnterTime_label.setBackground(Color.WHITE);
		exitCarEnterTime_label.setBounds(89, 78, 81, 36);
		exitCarFeeIs_label.add(exitCarEnterTime_label);

		exitCarExitTime_label = new JLabel("\uCD9C\uCC28\uC2DC\uAC04");
		exitCarExitTime_label.setOpaque(true);
		exitCarExitTime_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarExitTime_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarExitTime_label.setBackground(Color.WHITE);
		exitCarExitTime_label.setBounds(89, 127, 81, 36);
		exitCarFeeIs_label.add(exitCarExitTime_label);

		exitCarType_label = new JLabel("\uAD6C\uBD84");
		exitCarType_label.setOpaque(true);
		exitCarType_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarType_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarType_label.setBackground(Color.WHITE);
		exitCarType_label.setBounds(89, 225, 81, 36);
		exitCarFeeIs_label.add(exitCarType_label);

		exitCarEnterTimeHour_txtfld = new JTextField();
		exitCarEnterTimeHour_txtfld.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarEnterTimeHour_txtfld.setColumns(10);
		exitCarEnterTimeHour_txtfld.setDocument(new JTextFieldLimit(2));
		exitCarEnterTimeHour_txtfld.setBounds(203, 78, 39, 36);
		exitCarFeeIs_label.add(exitCarEnterTimeHour_txtfld);

		exitCarEnterTimeHour_label = new JLabel("\uC2DC");
		exitCarEnterTimeHour_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarEnterTimeHour_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 18));
		exitCarEnterTimeHour_label.setOpaque(true);
		exitCarEnterTimeHour_label.setBackground(Color.WHITE);
		exitCarEnterTimeHour_label.setBounds(246, 78, 22, 36);
		exitCarFeeIs_label.add(exitCarEnterTimeHour_label);

		exitCarEnterTimeMin_txtfld = new JTextField();
		exitCarEnterTimeMin_txtfld.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarEnterTimeMin_txtfld.setColumns(10);
		exitCarEnterTimeMin_txtfld.setDocument(new JTextFieldLimit(2));
		exitCarEnterTimeMin_txtfld.setBounds(280, 78, 39, 36);
		exitCarFeeIs_label.add(exitCarEnterTimeMin_txtfld);

		exitCarEnterTimeMin_label = new JLabel("\uBD84");
		exitCarEnterTimeMin_label.setOpaque(true);
		exitCarEnterTimeMin_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarEnterTimeMin_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 18));
		exitCarEnterTimeMin_label.setBackground(Color.WHITE);
		exitCarEnterTimeMin_label.setBounds(321, 78, 22, 36);
		exitCarFeeIs_label.add(exitCarEnterTimeMin_label);

		exitCarExitTimeHour_txtfld = new JTextField();
		exitCarExitTimeHour_txtfld.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarExitTimeHour_txtfld.setColumns(10);
		exitCarExitTimeHour_txtfld.setDocument(new JTextFieldLimit(2));
		exitCarExitTimeHour_txtfld.setBounds(203, 127, 39, 36);
		exitCarFeeIs_label.add(exitCarExitTimeHour_txtfld);

		exitCarExitTimeHour_label = new JLabel("\uC2DC");
		exitCarExitTimeHour_label.setOpaque(true);
		exitCarExitTimeHour_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarExitTimeHour_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 18));
		exitCarExitTimeHour_label.setBackground(Color.WHITE);
		exitCarExitTimeHour_label.setBounds(246, 127, 22, 36);
		exitCarFeeIs_label.add(exitCarExitTimeHour_label);

		exitCarExitTimeMin_txtfld = new JTextField();
		exitCarExitTimeMin_txtfld.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarExitTimeMin_txtfld.setColumns(10);
		exitCarExitTimeMin_txtfld.setDocument(new JTextFieldLimit(2));
		exitCarExitTimeMin_txtfld.setBounds(280, 127, 39, 36);
		exitCarFeeIs_label.add(exitCarExitTimeMin_txtfld);

		exitCarExitTimeMin_label = new JLabel("\uBD84");
		exitCarExitTimeMin_label.setOpaque(true);
		exitCarExitTimeMin_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarExitTimeMin_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 18));
		exitCarExitTimeMin_label.setBackground(Color.WHITE);
		exitCarExitTimeMin_label.setBounds(321, 127, 22, 36);
		exitCarFeeIs_label.add(exitCarExitTimeMin_label);

		exitCarElapseTime_label = new JLabel("\uACBD\uACFC\uC2DC\uAC04");
		exitCarElapseTime_label.setOpaque(true);
		exitCarElapseTime_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarElapseTime_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarElapseTime_label.setBackground(Color.WHITE);
		exitCarElapseTime_label.setBounds(89, 176, 81, 36);
		exitCarFeeIs_label.add(exitCarElapseTime_label);

		exitCarFee_label = new JLabel("\uC694\uAE08");
		exitCarFee_label.setOpaque(true);
		exitCarFee_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarFee_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarFee_label.setBackground(Color.WHITE);
		exitCarFee_label.setBounds(89, 274, 81, 36);
		exitCarFeeIs_label.add(exitCarFee_label);

		exitCarType_cbobx = new JLabel();
		exitCarType_cbobx.setOpaque(true);
		exitCarType_cbobx.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarType_cbobx.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarType_cbobx.setBackground(Color.WHITE);
		exitCarType_cbobx.setBounds(203, 225, 140, 36);
		exitCarFeeIs_label.add(exitCarType_cbobx);

		exitCarElapseTimeShowMin_label = new JLabel("\uBD84");
		exitCarElapseTimeShowMin_label.setOpaque(true);
		exitCarElapseTimeShowMin_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarElapseTimeShowMin_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 18));
		exitCarElapseTimeShowMin_label.setBackground(Color.WHITE);
		exitCarElapseTimeShowMin_label.setBounds(321, 176, 22, 36);
		exitCarFeeIs_label.add(exitCarElapseTimeShowMin_label);

		exitCarFeeShow_label = new JLabel();
		exitCarFeeShow_label.setOpaque(true);
		exitCarFeeShow_label.setHorizontalAlignment(SwingConstants.CENTER);
		exitCarFeeShow_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		exitCarFeeShow_label.setBackground(Color.WHITE);
		exitCarFeeShow_label.setBounds(203, 274, 140, 36);
		exitCarFeeIs_label.add(exitCarFeeShow_label);

		exitCarEdit_btn = new JButton("\uC0C8\uB85C\uACE0\uCE68");
		exitCarEdit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					//수정시 차량번호 이상이 있으면 arithmetic Exception 발생시킴
					int check = ModifyFeeIsPanel(freeCar_Table);
					if (check == 1) {
						int i = 0;
						int j = 10 / i;
					}

				} catch (ArithmeticException e) {
					// JOptionPane.showMessageDialog(null, "오류입니다 다시 입력해주세요", "경고",
					// JOptionPane.WARNING_MESSAGE);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "알수없는 오류입니다 다시 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		exitCarEdit_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		exitCarEdit_btn.setBounds(391, 287, 97, 25);
		exitCarFeeIs_label.add(exitCarEdit_btn);

		// 수정/출차 패널 내 출차처리 버튼 클릭했을경우
		JButton exitCarConfirmed_btn = new JButton("\uCD9C\uCC28\uCC98\uB9AC");
		exitCarConfirmed_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String entHr, entMn, extHr, extMn;
				// carNum = exitCarNum_txtfld.getText();

				entHr = exitCarEnterTimeHour_txtfld.getText();
				entMn = exitCarEnterTimeMin_txtfld.getText();
				extHr = exitCarExitTimeHour_txtfld.getText();
				extMn = exitCarExitTimeMin_txtfld.getText();

				try {

					int check = ModifyFeeIsPanel(freeCar_Table);
					if (check == 1) {
						int i = 0;
						int j = 10 / i;
					}
					//사유가 있을경우 유료라도 무료 처리
					if (reason != null) {
						exitCarFeeShow_label.setText("0원");
					}
					// 이상이 없을 경우 확인 메세지 띄움
					int input = JOptionPane.showOptionDialog(null,
							"차량번호: " + exitCarNum_txtfld.getText() + "\n입차시간: " + entHr + ":" + entMn + "\n출차시간: "
									+ extHr + ":" + extMn + "\n요금: " + exitCarFeeShow_label.getText()
									+ "\n 출차처리 하시겠습니까?",
							"출차확인", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					
					// 확인이 될경우 출차 데이터 생성
					if (input == JOptionPane.OK_OPTION) {

						String data[] = new String[6];
						data[0] = exitCarType_cbobx.getText();
						if (reason != null) {
							data[0] = "무료차량";
						}
						data[1] = exitCarNum_txtfld.getText();
						data[2] = (entHr + "시" + entMn + "분");
						data[3] = (extHr + "시" + extMn + "분");
						data[4] = exitCarFeeShow_label.getText();
						data[5] = reason;
						dtm.addRow(data);
						reason = null;

						// System.out.println(dtm.getRowCount());
						
						
						// 출차된 데이터는 테이블에서 지움
						if (NonfreeCar_Table.getSelectedRow() != -1) {
							NonfreeCar_Model.removeRow(NonfreeCar_Table.getSelectedRow());
						} else {
							freeCar_Model.removeRow(freeCar_Table.getSelectedRow());
						}
						DisableExitCarPage(freeCar_Table);

						if (checkAlertOverCar(freeCar_Table) == false) {
							carTimeALert_label.setText("");
							// alertLabelFlag = 0;
						}
					} else {
						reason = null;
						int i = 0;
						int j = 10 / i;
					}
				} catch (ArithmeticException e) {
					// JOptionPane.showMessageDialog(null, "오류입니다 다시 입력해주세요", "경고",
					// JOptionPane.WARNING_MESSAGE);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "알수없는 오류입니다 다시 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		exitCarConfirmed_btn.setBounds(280, 325, 99, 25);
		exitCarFeeIs_label.add(exitCarConfirmed_btn);
		exitCarConfirmed_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		
		
		// 수정/출차 패널에 수정 버튼 클릭 했을 경우 정보 수정
		exitCarDataModify_btn = new JButton("\uC218\uC815");
		exitCarDataModify_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					
					// 사유는 출차처리 버튼 클릭시에만 활성화 가능
					if (reason != null) {
						JOptionPane.showMessageDialog(null, "사유입력은 출차시에만 가능합니다.\n사유입력란을 null로 초기화합니다", "경고",
								JOptionPane.WARNING_MESSAGE);
						reason = null;
						int i = 0;
						int j = 10 / i;
					}
					int check = ModifyFeeIsPanel(freeCar_Table);
					if (check == 1) {
						int i = 0;
						int j = 10 / i;
					}
					//유료 주차 테이블이 선택돼 있을때
					if (NonfreeCar_Table.getSelectedRow() != -1) {
						NonfreeCar_Table.setValueAt(entHr + "시" + entMn + "분" + "0초", NonfreeCar_Table.getSelectedRow(),
								0);
						//무료주차가 선택 돼 있을때
					} else if (freeCar_Table.getSelectedRow() != -1) {
						freeCar_Table.setValueAt(entHr + "시" + entMn + "분" + "0초", freeCar_Table.getSelectedRow(), 0);

					}

					DisableExitCarPage(freeCar_Table);

				} catch (ArithmeticException e) {
					// JOptionPane.showMessageDialog(null, "오류입니다 다시 입력해주세요", "경고",
					// JOptionPane.WARNING_MESSAGE);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "알수없는 오류입니다 다시 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		exitCarDataModify_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		exitCarDataModify_btn.setBounds(171, 325, 97, 25);
		exitCarFeeIs_label.add(exitCarDataModify_btn);

		ExitCarElapseTimeShow_label = new JLabel();
		ExitCarElapseTimeShow_label.setOpaque(true);
		ExitCarElapseTimeShow_label.setHorizontalAlignment(SwingConstants.CENTER);
		ExitCarElapseTimeShow_label.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 17));
		ExitCarElapseTimeShow_label.setBackground(Color.WHITE);
		ExitCarElapseTimeShow_label.setBounds(203, 176, 116, 36);
		exitCarFeeIs_label.add(ExitCarElapseTimeShow_label);

		
		//수정 출차 패널에서 ? 버튼
		// 이버튼은 관리자가 없을때 출차를 하여 돈을 받지 못하고 출차 시간도 없을때 처리
		btnNewButton = new JButton("?");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitCarExitTimeHour_txtfld.setText("0");
				exitCarExitTimeMin_txtfld.setText("0");
				exitCarType_cbobx.setText("무료차량");
				exitCarFeeShow_label.setText("0원");
			}
		});
		btnNewButton.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 13));
		btnNewButton.setBounds(351, 127, 41, 36);
		exitCarFeeIs_label.add(btnNewButton);
		
		
		// 수정 출차 패널에서 사유 버튼
		// 사유를 입력하면 유료주차 차량이 무료로 바뀐다.
		JButton exitCarReason_btn = new JButton("\uC0AC\uC720\uC785\uB825");

		exitCarReason_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (exitCarType_cbobx.getText().contains("무료")) {
					JOptionPane.showMessageDialog(null, "이미 무료차량 입니다", "경고", JOptionPane.WARNING_MESSAGE);
				} else {
					reason = JOptionPane.showInputDialog(null, "사유를 입력해주세요" + "\n" + "유료차량에 사유가 입력될 경우 무료차량으로 처리됩니다");
				}
			}
		});
		exitCarReason_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 15));
		exitCarReason_btn.setBounds(352, 225, 97, 36);
		exitCarFeeIs_label.add(exitCarReason_btn);

		//
		//
		//
		//
		//
		// 유로주차 판넬
		NonFree_Panel = new JPanel();
		NonFree_Panel.setBackground(Color.GRAY);
		NonFree_Panel.setBounds(35, 65, 353, 437);
		ParkingLot.add(NonFree_Panel);
		NonFree_Panel.setLayout(null);

		JLabel NonFree_Text = new JLabel("\uC720\uB8CC\uC8FC\uCC28\uB300\uC0C1");
		NonFree_Text.setBounds(0, 0, 353, 22);
		NonFree_Text.setHorizontalAlignment(SwingConstants.CENTER);
		NonFree_Text.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		NonFree_Panel.add(NonFree_Text);
		NonfreeCar_Table = new JTable(NonfreeCar_Model);
		NonfreeCar_Table.getTableHeader().setReorderingAllowed(false);
		NonfreeCar_Table.setFont(new Font("나눔스퀘어라운드 Light", Font.PLAIN, 13));
		JScrollPane NonfreeCar_Scroll = new JScrollPane(NonfreeCar_Table);
		NonfreeCar_Scroll.setBounds(0, 22, 353, 363);
		NonFree_Panel.add(NonfreeCar_Scroll);

		NonfreeCarDelete_btn = new JButton("\uC0AD\uC81C");
		NonfreeCarDelete_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (NonfreeCar_Table.getSelectedRow() == -1)
					return;
				else
					NonfreeCar_Model.removeRow(NonfreeCar_Table.getSelectedRow());

			}
		});
		NonfreeCarDelete_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		NonfreeCarDelete_btn.setBounds(114, 399, 97, 25);
		NonFree_Panel.add(NonfreeCarDelete_btn);

		// 무료차 판넬
		Free_Panel = new JPanel();
		Free_Panel.setBackground(Color.GRAY);
		Free_Panel.setBounds(462, 65, 353, 437);
		ParkingLot.add(Free_Panel);
		Free_Panel.setLayout(null);

		JLabel Free_Text = new JLabel("\uBB34\uB8CC\uC8FC\uCC28\uB300\uC0C1");
		Free_Text.setBounds(0, 0, 353, 22);
		Free_Text.setHorizontalAlignment(SwingConstants.CENTER);
		Free_Text.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		Free_Panel.add(Free_Text);

		freeCar_Table.setFont(new Font("나눔스퀘어라운드 Light", Font.PLAIN, 13));
		freeCar_Table.getTableHeader().setReorderingAllowed(false);
		JScrollPane freeCar_Scroll = new JScrollPane(freeCar_Table);
		freeCar_Scroll.setBounds(0, 22, 353, 364);
		Free_Panel.add(freeCar_Scroll);

		JButton freeCarDelete_btn = new JButton("\uC0AD\uC81C");
		freeCarDelete_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		freeCarDelete_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (freeCar_Table.getSelectedRow() == -1)
					return;
				else
					freeCar_Model.removeRow(freeCar_Table.getSelectedRow());

				if (checkAlertOverCar(freeCar_Table) == false) {
					carTimeALert_label.setText("");
					// alertLabelFlag = 0;
				}
			}
		});
		freeCarDelete_btn.setBounds(211, 399, 97, 25);
		Free_Panel.add(freeCarDelete_btn);
		
		// 영수증 발급 버튼
		// 영수증을 발급하게 되면 무료주차에서 유료주차 테이블로 넘어감
		JButton gotoNonFree_btn = new JButton("\uC601\uC218\uC99D\uBC1C\uAE09");
		gotoNonFree_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 12));
		gotoNonFree_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (freeCar_Table.getSelectedRow() == -1)
					return;
				else {
					String changeCarData[] = new String[3];
					changeCarData[0] = (String) freeCar_Table.getValueAt(freeCar_Table.getSelectedRow(), 0);
					changeCarData[1] = (String) freeCar_Table.getValueAt(freeCar_Table.getSelectedRow(), 1);
					changeCarData[2] = (String) freeCar_Table.getValueAt(freeCar_Table.getSelectedRow(), 2);

					freeCar_Model.removeRow(freeCar_Table.getSelectedRow());
					NonfreeCar_Model.addRow(changeCarData);
				}

				if (checkAlertOverCar(freeCar_Table) == false) {
					carTimeALert_label.setText("");
					// alertLabelFlag = 0;
				}
			}
		});
		gotoNonFree_btn.setBounds(51, 399, 97, 25);
		Free_Panel.add(gotoNonFree_btn);

		// 현재시간변수

		// 입차 판넬
		//
		//
		//
		//

		JPanel NewCar_Panel = new JPanel();
		NewCar_Panel.setBounds(401, 514, 434, 52);
		ParkingLot.add(NewCar_Panel);
		NewCar_Panel.setLayout(null);

		NewCar_TextField = new JTextField();
		NewCar_TextField.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		NewCar_TextField.setBounds(26, 13, 285, 26);
		NewCar_Panel.add(NewCar_TextField);
		NewCar_TextField.setColumns(4);
		NewCar_TextField.setDocument((new JTextFieldLimit(4)));
		
		
		// 입차 버튼 클릭시 
		JButton NewCar_Btn = new JButton("\uC785\uCC28");
		NewCar_Btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));
		NewCar_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String carNum = NewCar_TextField.getText();
				if (checkSameCar(freeCar_Table, carNum)) {
					JOptionPane.showMessageDialog(null, "동일한 차량번호가 존재합니다", "경고", JOptionPane.WARNING_MESSAGE);
				} else if (carNum.equals("")) {
					JOptionPane.showMessageDialog(null, "차량번호를 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
				} else if (carNum.length() < 4) {
					JOptionPane.showMessageDialog(null, "차량번호를 4자리로 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
				} else {
					String freeCarData[] = new String[3];

					freeCarData[0] = new String(hour + "시" + min + "분" + second + "초");
					freeCarData[1] = carNum.toString();
					freeCarData[2] = "0";
					freeCar_Model.addRow(freeCarData);

					NewCar_TextField.setText("");

				}
			}
		});

		NewCar_Btn.setBounds(323, 14, 97, 25);
		NewCar_Panel.add(NewCar_Btn);
		
				carTimeALert_label = new JLabel();
				carTimeALert_label.setForeground(Color.RED);
				carTimeALert_label.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
				// carTimeALert_label.setText("");
				carTimeALert_label.setBounds(462, 13, 334, 39);
				ParkingLot.add(carTimeALert_label);

		btnpanel = new JPanel();
		btnpanel.setBounds(0, 0, 860, 593);
		ParkingLot.add(btnpanel);
		btnpanel.setLayout(null);

		JButton unselect_btn = new JButton("\uC120\uD0DD\uD574\uC81C");
		unselect_btn.setBounds(37, 528, 97, 25);
		btnpanel.add(unselect_btn);
		unselect_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));

		JButton carExit_btn = new JButton("\uC218\uC815/\uCD9C\uCC28");
		carExit_btn.setBounds(149, 528, 97, 25);
		btnpanel.add(carExit_btn);
		carExit_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));

		ExitCarList_btn = new JButton("\uCD9C\uCC28\uB0B4\uC5ED");
		ExitCarList_btn.setBounds(717, 18, 97, 25);
		btnpanel.add(ExitCarList_btn);
		ExitCarList_btn.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 13));

		clockNow_label = new JLabel("\uD604\uC7AC\uC2DC\uAC04: ");
		clockNow_label.setFont(new Font("나눔스퀘어라운드 Light", Font.PLAIN, 13));
		clockNow_label.setBounds(37, 18, 154, 16);
		btnpanel.add(clockNow_label);

		btnNewButton_1 = new JButton("\uADFC\uBB34\uC790:");
		btnNewButton_1.setFont(new Font("나눔스퀘어라운드 Light", Font.PLAIN, 13));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmployeeName = JOptionPane.showInputDialog("근무자 이름을 입력해주세요");
				btnNewButton_1.setText("근무자: " + EmployeeName);

			}
		});
		btnNewButton_1.setHorizontalAlignment(SwingConstants.LEADING);
		btnNewButton_1.setBounds(37, 37, 119, 25);
		btnpanel.add(btnNewButton_1);
		ExitCarList_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExitCarData();
			}
		});
		//수정 출차 버튼을 누르면 오류를 띄우거나 출차 판넬을 띄움
		carExit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (NonfreeCar_Table.getSelectedRow() == -1 && freeCar_Table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "선택된 차량내역이 없습니다", "경고", JOptionPane.WARNING_MESSAGE);
				} else if (NonfreeCar_Table.getSelectedRow() != -1 && freeCar_Table.getSelectedRow() != -1) {
					JOptionPane.showMessageDialog(null, "하나의 차량만 선택해 주세요", "경고", JOptionPane.WARNING_MESSAGE);
				} else {
					AbleExitCarPage();
					if (NonfreeCar_Table.getSelectedRow() != -1) {
						setExitCarPanel(NonfreeCar_Table, NonfreeCar_Table.getSelectedRow(), false);
					} else {
						setExitCarPanel(freeCar_Table, freeCar_Table.getSelectedRow(), false);
					}
				}
			}
		});
		// 선택취소 버튼을 누르는 경우 테이블 내 선택된 커서가 없어짐.
		unselect_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freeCar_Table.clearSelection();
				NonfreeCar_Table.clearSelection();
			}
		});
		clock();
		setElapseTime(freeCar_Table);
		alertOverCar(freeCar_Table);

		
	}

}
