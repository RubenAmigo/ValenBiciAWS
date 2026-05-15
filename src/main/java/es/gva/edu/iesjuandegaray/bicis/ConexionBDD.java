package es.gva.edu.iesjuandegaray.bicis;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;

public class ConexionBDD extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textAreaDatos;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;



	
	// Declaramos la conexion a mysql
	 private static Connection con;
	 private static Statement s;
	 private static DatosJSon dJSon;
	 private static int numEst = 3;


	 private static final String driver="com.mysql.cj.jdbc.Driver";
	 private static final String user="root";
	 private static final String pass="administrador";
	 private static final String url="jdbc:mysql://localhost:3306/valenbicibd";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConexionBDD frame = new ConexionBDD();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConexionBDD() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 544, 381);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Introduce el numero de estaciones a consultar: ");
		lblNewLabel.setBounds(52, 37, 312, 12);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(379, 34, 141, 18);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButtonDatos = new JButton("Datos");
		btnNewButtonDatos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        numEst = Integer.parseInt(textField.getText());
		        dJSon = new DatosJSon(numEst);
		        dJSon.mostrarDatos(numEst);
		        textAreaDatos.setText(dJSon.getDatos());
		    }
		});
		
		btnNewButtonDatos.setBounds(10, 72, 115, 20);
		contentPane.add(btnNewButtonDatos);
		
		textAreaDatos = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textAreaDatos);
		scrollPane.setBounds(135, 70, 385, 153);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane);
		
		JButton btnNewButtonConectarBDD = new JButton("Conectar");
		btnNewButtonConectarBDD.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        con = conector();
		        if (con != null) {
		            lblNewLabel_1.setText("Conexión establecida");
		        } else {
		            lblNewLabel_1.setText("Error de conexión");
		        }
		    }
		});
		btnNewButtonConectarBDD.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    }
		});
		btnNewButtonConectarBDD.setBounds(10, 240, 114, 20);
		contentPane.add(btnNewButtonConectarBDD);
		
		JButton btnNewButtonAñadir = new JButton("Añadir a BDD");
		btnNewButtonAñadir.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (con == null) {
		            lblNewLabel_1.setText("Primero conecta a la BDD");
		            return;
		        }
		        try {
		            for (int i = 0; i < dJSon.getNumEst(); i++) {
		                String[] datos = dJSon.getValues()[i].split(",");
		                String sql = "INSERT INTO historico (estacion_id, direccion, bicis_disponibles, anclajes_libres, estado_operativo) "
		                        + "VALUES (" + i + ", '" + datos[0] + "', " + datos[1] + ", " + datos[2] + ", 1)";
		                s.executeUpdate(sql);
		            }
		            lblNewLabel_2.setText("Datos añadidos correctamente");
		        } catch (SQLException ex) {
		            lblNewLabel_2.setText("Error al añadir datos");
		            ex.printStackTrace();
		        }
		    }
		});
		btnNewButtonAñadir.setBounds(10, 272, 114, 20);
		contentPane.add(btnNewButtonAñadir);
		
		lblNewLabel_1 = new JLabel("Estado conexion:");
		lblNewLabel_1.setBounds(134, 244, 261, 12);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Primero Obtener Datos de Estaciones y Conectar con BDD.");
		lblNewLabel_2.setBounds(134, 276, 339, 12);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButtonCerrar = new JButton("Cerrar Conexión");
		btnNewButtonCerrar.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        try {
		            if (con != null) {
		                con.close();
		                con = null;
		                lblNewLabel_1.setText("Conexión cerrada");
		            }
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }
		    }

		});
		btnNewButtonCerrar.setBounds(166, 314, 172, 20);
		contentPane.add(btnNewButtonCerrar);

	}

	public Connection conector() {
	    con = null;
	    try {
	        Class.forName(driver);
	        con = (Connection) DriverManager.getConnection(url, user, pass);
	        s = con.createStatement();
	        return con;
	    } catch (ClassNotFoundException | SQLException e) {
	        return null;
	    }
	}

}
