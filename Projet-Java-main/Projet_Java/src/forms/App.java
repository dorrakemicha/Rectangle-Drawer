package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Il s'agit du code Principal du Projet.
public class App extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -74124200340777339L;
	private boolean unitedDragging = false;
    private JButton button;
    private JPanel drawingPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<RectangleBounds> rectangles;

    private Point initialMousePoint;
    private RectangleBounds selectedRectangle;
    
    /**
     * Constructeur de la classe Rectangle.
     */
    public App() {
        setTitle("Rectangle Drawer");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermeture de l'application lors de la fermeture de la fenêtre

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        rectangles = new ArrayList<>();

        button = new JButton("Clear");
        button.setPreferredSize(new Dimension(80, 30));

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(button);
        add(buttonPanel, BorderLayout.SOUTH);

        drawingPanel = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -5957560708419081394L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (RectangleBounds bounds : rectangles) {
                    if (bounds.isSelected()) {
                        g.setColor(Color.RED); // Bordure rouge pour le rectangle sélectionné
                        int x = Math.min(bounds.getStartPoint().x, bounds.getEndPoint().x);
                        int y = Math.min(bounds.getStartPoint().y, bounds.getEndPoint().y);
                        int width = Math.abs(bounds.getEndPoint().x - bounds.getStartPoint().x);
                        int height = Math.abs(bounds.getEndPoint().y - bounds.getStartPoint().y);
                        g.drawRect(x, y, width, height); // Dessiner une bordure autour du rectangle sélectionné
                    }
                    drawRectangle(g, bounds);
                }
            }
        };
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (rectangles.size() == 0 || rectangles.get(rectangles.size() - 1).isComplete()) {
                    rectangles.add(new RectangleBounds(e.getPoint()));
                } else {
                    rectangles.get(rectangles.size() - 1).setEndPoint(e.getPoint());
                    updateTable();
                }
                drawingPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (RectangleBounds bounds : rectangles) {
                    if (bounds.contains(e.getPoint())) {
                        bounds.setSelected(true);
                        initialMousePoint = e.getPoint();
                        selectedRectangle = bounds;
                    } else {
                        bounds.setSelected(false);
                    }
                }
                drawingPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedRectangle != null) {
                    selectedRectangle.setSelected(false);
                    selectedRectangle = null;
                    drawingPanel.repaint();
                }
            }
        });

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedRectangle != null) {
                    int dx = e.getX() - initialMousePoint.x;
                    int dy = e.getY() - initialMousePoint.y;

                    if(unitedDragging) {
                        // Faire bouger tout les rectangles
                        for(RectangleBounds rect : rectangles) {
                            rect.move(dx, dy);
                        }
                    } else {
                        // Bouger seulement le selectionne
                        selectedRectangle.move(dx, dy);
                    }

                    initialMousePoint = e.getPoint();
                    drawingPanel.repaint();
                }
            }
        });


        add(drawingPanel, BorderLayout.CENTER);

     // Modèle de tableau pour afficher les dimensions et les couleurs des rectangles
        tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 4780554119253062280L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
                RectangleBounds rect = rectangles.get(row);
                if (column == 0) {
                    int width = Integer.parseInt(aValue.toString());
                    rect.updateDimensions(width, rect.getHeight());
                } else if (column == 1) {
                    int height = Integer.parseInt(aValue.toString());
                    rect.updateDimensions(rect.getWidth(), height);
                } else if (column == 2) {
                    rect.setColor((Color) aValue);
                }
                drawingPanel.repaint();
            }
        };

        tableModel.addColumn("Width");
        tableModel.addColumn("Height");
        tableModel.addColumn("Color");

        table = new JTable(tableModel);
        table.getColumnModel().getColumn(2).setCellEditor(new ColorComboBoxEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.EAST);

        button.addActionListener(e -> {
            rectangles.clear();
            tableModel.setRowCount(0);
            drawingPanel.repaint();
        });

        createMenuBar();
    }
    
    public static RectanglePair calculateUnion(RectangleBounds rect1, RectangleBounds rect2) {
        int minX = Math.min(rect1.getStartPoint().x, rect2.getStartPoint().x);
        int minY = Math.min(rect1.getStartPoint().y, rect2.getStartPoint().y);
        int maxX = Math.max(rect1.getEndPoint().x, rect2.getEndPoint().x);
        int maxY = Math.max(rect1.getEndPoint().y, rect2.getEndPoint().y);

        RectangleBounds union = new RectangleBounds(new java.awt.Point(minX, minY));
        union.setEndPoint(new java.awt.Point(maxX, maxY));
        union.setColor(rect1.getColor()); // Choisir la couleur

        //Calculer le décalage entre les deux rectangles
        int offsetX = rect1.getStartPoint().x - rect2.getStartPoint().x;
        int offsetY = rect1.getStartPoint().y - rect2.getStartPoint().y;

        System.out.println("OffsetX: " + offsetX + ", OffsetY: " + offsetY); // Print offset

        RectanglePair pair = new RectanglePair(rect1, rect2);
        pair.setOffset(offsetX, offsetY);

        return pair;
    }


    /**
     * Crée la barre de menu de l'application.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem intersectMenuItem = new JMenuItem("Intersect");
      //JMenuItem SubstractMenuItem = new JMenuItem("Substract");
        JMenuItem unitedDraggingMenuItem = new JMenuItem("Toggle United Dragging"); // Nouvel élément de menu pour activer/désactiver le united dragging

        saveMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    Saver.saveDrawing(rectangles, filename);
                    JOptionPane.showMessageDialog(this, "Drawing saved successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to save drawing: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    rectangles = Loader.loadDrawing(filename);
                    updateTable();
                    drawingPanel.repaint();
                    JOptionPane.showMessageDialog(this, "Drawing loaded successfully!");
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load drawing: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        intersectMenuItem.addActionListener(e -> {
            if (rectangles.size() >= 2) {
                Area intersection = IntersectionCalculator.calculateIntersection(rectangles.get(0), rectangles.get(1));
                rectangles.clear();
                rectangles.add(new RectangleBounds(new Point(0, 0))); //Point de depart
                rectangles.get(0).setEndPoint(new Point((int) intersection.getBounds().getWidth(), (int) intersection.getBounds().getHeight()));
                updateTable();
                drawingPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please draw at least two rectangles to intersect.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
      /*SubstractMenuItem.addActionListener(e -> {
            if (rectangles.size() >= 2) {
                Area difference = forms.Difference.Difference(rectangles.get(0), rectangles.get(1));
                rectangles.clear();
                rectangles.add(new RectangleBounds(new Point(0, 0))); // Point de départ fictif
                rectangles.get(0).setEndPoint(new Point((int) difference.getBounds().getWidth(), (int) difference.getBounds().getHeight()));
                updateTable();
                drawingPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please draw at least two rectangles to find the difference.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }); */
        unitedDraggingMenuItem.addActionListener(e -> {
            if(unitedDragging) {
                unitedDragging = false; // Desactive united dragging
                JOptionPane.showMessageDialog(this, "United dragging disabled.");
            } else {
                unitedDragging = true; // Active united dragging
                JOptionPane.showMessageDialog(this, "United dragging enabled.");
            }
        });

        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(intersectMenuItem);
      //fileMenu.add(SubstractMenuItem);
        fileMenu.add(unitedDraggingMenuItem); // Ajout de l'élément pour activer/désactiver l'union
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

   

    private void drawRectangle(Graphics g, RectangleBounds bounds) {
        if (!bounds.isComplete()) return;
        int x = Math.min(bounds.getStartPoint().x, bounds.getEndPoint().x);
        int y = Math.min(bounds.getStartPoint().y, bounds.getEndPoint().y);
        int width = Math.abs(bounds.getEndPoint().x - bounds.getStartPoint().x);
        int height = Math.abs(bounds.getEndPoint().y - bounds.getStartPoint().y);
        g.setColor(bounds.getColor());
        g.fillRect(x, y, width, height);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (RectangleBounds rect : rectangles) {
            tableModel.addRow(new Object[]{rect.getWidth(), rect.getHeight(), rect.getColor()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }

    public static class RectangleBounds implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 2476181497529900470L;
		private Point startPoint;
        private Point endPoint;
        private Color color = Color.DARK_GRAY;
        private boolean selected = false; // Nouvelle variable pour suivre l'état de sélection

        public RectangleBounds(Point startPoint) {
            this.startPoint = startPoint;
        }

        public void setEndPoint(Point endPoint) {
            this.endPoint = endPoint;
        }

        public boolean isComplete() {
            return endPoint != null;
        }

        public Point getStartPoint() {
            return startPoint;
        }

        public Point getEndPoint() {
            return endPoint;
        }

        public int getWidth() {
            return Math.abs(endPoint.x - startPoint.x);
        }

        public int getHeight() {
            return Math.abs(endPoint.y - startPoint.y);
        }

        public void updateDimensions(int width, int height) {
            this.endPoint = new Point(startPoint.x + width, startPoint.y + height);
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public boolean contains(Point point) {
            int x = Math.min(startPoint.x, endPoint.x);
            int y = Math.min(startPoint.y, endPoint.y);
            int width = Math.abs(startPoint.x- endPoint.x);
            int height = Math.abs(startPoint.y - endPoint.y);

            return (point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height);
        }

        public void move(int dx, int dy) {
            startPoint.x += dx;
            startPoint.y += dy;
            endPoint.x += dx;
            endPoint.y += dy;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private static class ColorComboBoxEditor extends DefaultCellEditor {
        /**
		 * 
		 */
		private static final long serialVersionUID = -3060363761411323819L;
		private JComboBox<String> comboBox;

        @SuppressWarnings("unchecked")
		public ColorComboBoxEditor() {
            super(new JComboBox<>(new String[]{"Dark Gray", "Red", "Blue"}));
            this.comboBox = (JComboBox<String>) getComponent();
            comboBox.setEditable(false);
            comboBox.addActionListener(e -> stopCellEditing());
        }

        @Override
        public Object getCellEditorValue() {
            String selectedColor = (String) comboBox.getSelectedItem();
            return switch (selectedColor) {
                case "Red" -> Color.RED;
                case "Blue" -> Color.BLUE;
                default -> Color.DARK_GRAY;
            };
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Color) {
                comboBox.setSelectedItem(getColorName((Color) value));
            }
            return comboBox;
        }

        private String getColorName(Color color) {
            if (Color.RED.equals(color)) return "Red";
            if (Color.BLUE.equals(color)) return "Blue";
            return "Dark Gray";
        }
    }
}