package fr.mrtigreroux.tigerreportssupports.listeners;

import java.util.Objects;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.mrtigreroux.tigerreports.events.NewReportEvent;
import fr.mrtigreroux.tigerreports.events.ProcessReportEvent;
import fr.mrtigreroux.tigerreports.events.ReportStatusChangeEvent;
import fr.mrtigreroux.tigerreports.managers.BungeeManager;
import fr.mrtigreroux.tigerreports.objects.reports.Report;
import fr.mrtigreroux.tigerreports.utils.ConfigUtils;
import fr.mrtigreroux.tigerreports.utils.MessageUtils;
import fr.mrtigreroux.tigerreportssupports.bots.DiscordBot;
import fr.mrtigreroux.tigerreportssupports.config.ConfigFile;

/**
 * @author MrTigreroux
 */

public class ReportListener implements Listener {

	private final DiscordBot discordBot;
	private final BungeeManager bm;

	public ReportListener(DiscordBot bot, BungeeManager bm) {
		this.discordBot = Objects.requireNonNull(bot);
		this.bm = bm;
	}

	@EventHandler
	public void onNewReport(NewReportEvent e) {
		String reportServerName = e.getServer();
		if (notify(reportServerName)) {
			discordBot.notifyReport(reportServerName, e.getReport());
		}
	}

	@EventHandler
	public void onProcessReport(ProcessReportEvent e) {
		Report r = e.getReport();
		if (discordBot != null && notify(r))
			discordBot.notifyProcessReport(r, e.getStaff());
	}

	@EventHandler
	public void onReportStatusChange(ReportStatusChangeEvent e) {
		Report r = e.getReport();
		if (discordBot != null & notify(r)) {
			discordBot.updateReportStatus(r);
		}
	}

	private boolean notify(Report r) {
		return notify(MessageUtils.getServer(r.getOldLocation(Report.ParticipantType.REPORTER)));
	}

	private boolean notify(String reportServerName) {
		return !ConfigUtils.isEnabled(ConfigFile.CONFIG.get(), "Config.Discord.NotifyOnlyLocalReports")
		        || bm.getServerName().equals(reportServerName);
	}

}
