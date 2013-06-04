package br.com.controle.notificacao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import br.com.controle.dominio.Lancamento;
import br.com.controle.exceptions.DaoException;
import br.com.controle.persistencia.LancamentoDAO;
import br.com.controle.principal.LogonActivity;
import br.com.controle.principal.R;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Igor
 */
public class NotificacaoService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
        long delayInicial = 0;
        long periodo = 30;
        TimeUnit unit = TimeUnit.SECONDS;
        pool.scheduleAtFixedRate(new NotificacaoTask(), delayInicial, periodo, unit);

        return START_STICKY;
    }

    private void criarNotificacao(Lancamento lancamento, int id) {
        int icone = R.drawable.ic_launcher;
        String aviso = getString(R.string.aviso);
        long data = System.currentTimeMillis();

        Context context = getApplicationContext();
        Intent intent = new Intent(context, LogonActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
                Intent.FLAG_ACTIVITY_NEW_TASK);

        Notification notification = new Notification(icone, aviso, data);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.setLatestEventInfo(context, lancamento.getTitulo(),
                NumberFormat.getCurrencyInstance().format(lancamento.getValor()),
                pendingIntent);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(ns);
        notificationManager.notify(id, notification);
    }

    private class NotificacaoTask implements Runnable {

        public void run() {
            try {
                LancamentoDAO dao = new LancamentoDAO(getApplicationContext());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String hoje = sdf.format(new Date());
                List<Lancamento> lancamentos = dao.lancamentosAVencer(sdf.parse(hoje).getTime());

                int i = 0;
                for (Lancamento lancamento : lancamentos) {
                    criarNotificacao(lancamento, i);
                    i ++;
                }

            } catch (ParseException ex) {
            } catch (DaoException ex) {
                Log.e("[LOG] APP", ex.getMessage());
            }
        }
    }
}
