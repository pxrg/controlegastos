package br.com.controle.principal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import br.com.controle.adapter.ToastManager;
import br.com.controle.dominio.Usuario;
import br.com.controle.exceptions.DaoException;
import br.com.controle.persistencia.UsuarioDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogonActivity extends Activity {

    Button confirmar;
    EditText senha, confirmarSenha;
    Dialog dialog;
    Usuario usuario;
    UsuarioDAO dao;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        dao = new UsuarioDAO(this);
        try {
            usuario = dao.findUsuario();
            if (usuario != null) {
                setContentView(R.layout.logon);
                senha = (EditText) findViewById(R.id.senha);
                confirmar = (Button) findViewById(R.id.confirmar);
                confirmar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (usuario.getPassword().equals(senha.getText().toString())) {
                            startActivity(new Intent(LogonActivity.this, TabControleGastos.class));
                            finish();
                        } else {
                            ToastManager.show(getApplicationContext(), getResources().getString(R.string.error_password), ToastManager.ERROR);
                        }
                    }
                });
            } else {
                openDialogNovoUsuario();
            }
        } catch (DaoException ex) {
            Log.e("[LOG] APP", ex.getMessage());
        }
    }

    private void openDialogNovoUsuario() {
        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.primeiro_acesso);
        dialog.setContentView(R.layout.dialog_novo_usuario);
        senha = (EditText) dialog.findViewById(R.id.senha);
        confirmarSenha = (EditText) dialog.findViewById(R.id.confirmarSenha);
        confirmar = (Button) dialog.findViewById(R.id.confirmar);
        confirmar.setOnClickListener(usuarioOnClick);
        dialog.show();
    }
    
    private View.OnClickListener usuarioOnClick = new View.OnClickListener() {
        public void onClick(View view) {
            String pwd = senha.getText().toString();
            String pwdConf = confirmarSenha.getText().toString();
            if (pwd.isEmpty() || pwdConf.isEmpty()) {
                ToastManager.show(getApplicationContext(), getResources().getString(R.string.error_campos_vazios), ToastManager.WARNING);
                if (pwd.isEmpty()) {
                    senha.setHintTextColor(getResources().getColor(R.color.error));
                }
                if (pwdConf.isEmpty()) {
                    confirmarSenha.setHintTextColor(getResources().getColor(R.color.error));
                }
            } else if (!pwd.equals(pwdConf)) {
                ToastManager.show(getApplicationContext(), getResources().getString(R.string.error_password_confere), ToastManager.WARNING);
            } else {
                try {
                    if (dao.savePasswordUser(new Usuario(pwd))) {
                        startActivity(new Intent(LogonActivity.this, TabControleGastos.class));
                        dialog.dismiss();
                        ToastManager.show(getApplicationContext(), getResources().getString(R.string.sucesso_msg), ToastManager.INFORMATION);
                        finish();
                    }
                } catch (DaoException ex) {
                    Log.e("[LOG] APP", ex.getMessage());
                }
            }
        }
    };
}
