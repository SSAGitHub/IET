import com.rsicms.groovy.utils.*

// -----------------------------------------------------------------------

rsuite.login();
def util = new DeployCaUtils(rsuite);

// Types reflecting the IET TV hierarchy:

util.defineContentAssemblyType("iettv_video_related_files", "IET.tv Video Related Files", false, null)
util.defineContentAssemblyType("iettv_video_notes", "IET.tv Video Notes", false, null)

util.defineContentAssemblyType("iettv_video", "IET.tv Video Record", false, null)
util.defineContentAssemblyType("iettv_year", "IET.tv Year", false, "iettv_video")
util.defineContentAssemblyType("iettv_channel", "IET.tv Channel", false, "iettv_year")
util.defineContentAssemblyType("iettv", "IET.tv Domain", false, "iettv_channel")


rsuite.logout();