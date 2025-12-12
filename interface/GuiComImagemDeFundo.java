import javax.swing.*;
import java.awt.*;

public class GuiComImagemDeFundo extends JFrame {
    public GuiComImagemDeFundo() {
        // Configurar o layout do painel principal
        setLayout(new OverlayLayout(getContentPane()));

        // Carregar a imagem de fundo
        ImageIcon imagemFundo = new ImageIcon("house_plant.png");
        ImageIcon cameraIcon = new ImageIcon("camera.png");

        Image imagemRedimensionada = cameraIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon cameraIconRedimensionado = new ImageIcon(imagemRedimensionada);


        // Criar um rótulo para exibir a imagem
        JLabel labelFundo = new JLabel(imagemFundo);
        labelFundo.setBounds(0, 0, imagemFundo.getIconWidth(), imagemFundo.getIconHeight());

        // Criar botões
        JButton botao1 = new JButton(cameraIconRedimensionado);
        botao1.setBounds(0, 0, cameraIconRedimensionado.getIconWidth(), cameraIconRedimensionado.getIconHeight());

        // Adicionar os botões ao painel principal
        getContentPane().add(botao1);

        // Adicionar a imagem de fundo sobre os botões
        getContentPane().add(labelFundo);

        // Configurar as propriedades da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(imagemFundo.getIconWidth(), imagemFundo.getIconHeight());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GuiComImagemDeFundo::new);
    }

    
}






