package com.example

data class Channel(
    val id: String,
    val name: String,
    val url: String,
    val category: String,
    val isDash: Boolean = false,
    val description: String = "",
    val iconEmoji: String = "⚽"
)

object ChannelsData {
    val categories = listOf(
        "FIFA LIVE / World Cup",
        "beIN SPORTS Network",
        "ESPN Network",
        "DSports Network",
        "DAZN & TUDN Sports",
        "Sky & Fox Sports",
        "Adrenaline & Action",
        "South Asian Sports",
        "Local & General"
    )

    val channelsList = listOf(
        // Category 1: World Cup / Special Live
        Channel(
            "fifa_plus",
            "FIFA+",
            "https://a62dad94.wurl.com/master/f36d25e7e52f1ba8d7e56eb859c636563214f541/UmFrdXRlblRWLWV1X0ZJRkFQbHVzRW5nbGlzaF9ITFM/playlist.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "Official FIFA+ live streaming channel with matches, highlights, and documentaries",
            "⚽"
        ),
        Channel(
            "wc_be_in",
            "World Cup 2026 (beIN)",
            "http://starhub.pro/live/farhat-3379/67897-913379/744517.ts",
            "FIFA LIVE / World Cup",
            false,
            "Live World Cup broadcast feed in premium HD",
            "🏆"
        ),
        Channel(
            "wc_bioscope",
            "World Cup 2026 (Bioscope Plus)",
            "https://streamsportixa.ajmainearafat.workers.dev/live.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "Bioscope high-speed World Cup stream",
            "🌎"
        ),
        Channel(
            "wc_fox",
            "World Cup 2026 (Fox Sports)",
            "https://otte-tim.live.pv-cdn.net/pdx-nitro/live/clients/dash/enc/ajfoeddkbz/out/v1/b78800b9b2304879b15843f455836829/cenc.mpd",
            "FIFA LIVE / World Cup",
            true,
            "Fox Sports multi-angle World Cup broadcast",
            "🇺🇸"
        ),
        Channel(
            "wc_sky",
            "World Cup 2026 (Sky)",
            "https://d1211whpimeups.cloudfront.net/smil:rtbgo/chunklist.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "Sky high-bitrate live World Cup stream",
            "🇬🇧"
        ),
        Channel(
            "wc_hindi",
            "World Cup 2026 (Hindi)",
            "http://starhub.pro/live/farhat-3379/67897-913379/741567.ts",
            "FIFA LIVE / World Cup",
            false,
            "World Cup live coverage with Hindi Commentary",
            "🇮🇳"
        ),
        Channel(
            "wc_4k_1",
            "World Cup 2026 (4K Ultra)",
            "http://ytoxw6un.ottclub.xyz/iptv/KCUHA6DGYYVA8ZZFUPQV3KZH/6408/index.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "4K Resolution Ultra-smooth World Cup Stream",
            "✨"
        ),
        Channel(
            "wc_4k_2",
            "World Cup 2026 (4K Feed 2)",
            "http://starhub.pro/live/farhat-3379/67897-913379/745269.ts",
            "FIFA LIVE / World Cup",
            false,
            "High fidelity 4K primary production feed",
            "🔥"
        ),
        Channel(
            "wc_4k_3",
            "World Cup 2026 (4K Feed 3)",
            "http://starhub.pro/live/farhat-3379/67897-913379/745270.ts",
            "FIFA LIVE / World Cup",
            false,
            "High fidelity 4K secondary broadcast angle",
            "📣"
        ),
        Channel(
            "wc_1",
            "World Cup 2026 (Alternative)",
            "http://202.70.146.135:8000/play/a06s/index.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "Backup World Cup feed with alternative audio",
            "🚀"
        ),
        Channel(
            "caze_tv_br",
            "Caze TV BR (Main)",
            "https://dfr80qz435crc.cloudfront.net/MNOP/Amagi/Caze/Caze_TV_BR/Caze_TV.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "Prominent Portuguese stream covering primary matches",
            "🇧🇷"
        ),
        Channel(
            "caze_tv_1080p",
            "Caze TV BR (1080p)",
            "https://dfr80qz435crc.cloudfront.net/MNOP/Amagi/Caze/Caze_TV_BR/1080p-vtt/index.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "Full high definition Portuguese sports stream",
            "⚽"
        ),
        Channel(
            "caze_tv_720p",
            "Caze TV BR (720p)",
            "https://dfr80qz435crc.cloudfront.net/MNOP/Amagi/Caze/Caze_TV_BR/720p-vtt/index.m3u8",
            "FIFA LIVE / World Cup",
            false,
            "Compact bandwidth Portuguese sports stream",
            "📱"
        ),

        // Category 2: beIN SPORTS Network
        Channel(
            "bein_4k",
            "beIN SPORTS 4K",
            "http://go8knm.optikl.ink/X/index.php/Besyria3/video.m3u8",
            "beIN SPORTS Network",
            false,
            "beIN flagship 4K premium channel",
            "💎"
        ),
        Channel(
            "bein_extra_n_1080p",
            "beIN Sports Extra Ñ (1080p)",
            "https://bein-esp-xumo.amagi.tv/playlistR1080p.m3u8",
            "beIN SPORTS Network",
            false,
            "Premium Spanish sports channel in full HD",
            "🇪🇸"
        ),
        Channel(
            "bein_extra_n_720p",
            "beIN Sports Extra Ñ (720p)",
            "https://bein-esp-xumo.amagi.tv/playlistR720P.m3u8",
            "beIN SPORTS Network",
            false,
            "Spanish sports channel in 720p optimized resolution",
            "🇪🇸"
        ),
        Channel(
            "bein_us",
            "beIN Sports US",
            "http://23.237.104.106:8080/USA_BEIN/index.m3u8",
            "beIN SPORTS Network",
            false,
            "American regional sports broadcast feed",
            "🗽"
        ),
        Channel(
            "bein_xtra_720",
            "beIN Xtra (720p)",
            "https://d9ssxzmclhfo4.cloudfront.net/bein_sports720p.m3u8",
            "beIN SPORTS Network",
            false,
            "beIN secondary coverage channel",
            "📺"
        ),
        Channel(
            "bein_sports_n",
            "beIN Sports Ñ Local",
            "https://amg01334-beinsportsllc-beinxtraesp-localnow-aekzc.amagi.tv/playlist.m3u8",
            "beIN SPORTS Network",
            false,
            "beIN Sports Spanish localized broadcast",
            "🇪🇸"
        ),
        Channel(
            "bein_sports_n_hd2",
            "beIN Sports Ñ HD 2",
            "http://190.83.9.222:8000/play/ca129",
            "beIN SPORTS Network",
            false,
            "Alternative HD stream for beIN Sports",
            "🎥"
        ),
        Channel(
            "bein_sports_1_au",
            "beIN Sports 1 Australia",
            "https://otte.live.fly.ww.aiv-cdn.net/syd-nitro/live/clients/dash/enc/ghwcl6hv68/out/v1/83536910d8034e9b9895a20fbe1c1687/cenc.mpd",
            "beIN SPORTS Network",
            true,
            "Premium Australian sports feed coverage",
            "🇦🇺"
        ),
        Channel(
            "bein_sports_2_au",
            "beIN Sports 2 Australia",
            "https://otte.live.fly.ww.aiv-cdn.net/syd-nitro/live/clients/dash/enc/8m8cd46i1t/out/v1/83985c68e4174e90a58a1f2c024be4c9/cenc.mpd",
            "beIN SPORTS Network",
            true,
            "Australian sports alternative live feed",
            "🇦🇺"
        ),
        Channel(
            "bein_sports_3_au",
            "beIN Sports 3 Australia",
            "https://a83aivottlinear-a.akamaihd.net/OTTB/syd-nitro/live/clients/dash/enc/q4u5nwaogz/out/v1/18de6d3e65934f3a8de4358e69eab86c/cenc.mpd",
            "beIN SPORTS Network",
            true,
            "Australian sports third live feed",
            "🇦🇺"
        ),
        Channel(
            "bein_sports_1_1080p",
            "beIN Sports 1 (1080p Live)",
            "https://andro.226503.xyz/checklist/androstreamlivebs1.m3u8",
            "beIN SPORTS Network",
            false,
            "Flagship European broadcast feed",
            "🇫🇷"
        ),
        Channel(
            "bein_sports_2_1080p",
            "beIN Sports 2 (1080p Live)",
            "https://andro.226503.xyz/checklist/androstreamlivebs2.m3u8",
            "beIN SPORTS Network",
            false,
            "beIN Sports secondary 1080p live feed",
            "🎖️"
        ),
        Channel(
            "bein_sports_3_1080p",
            "beIN Sports 3 (1080p Live)",
            "https://andro.226503.xyz/checklist/androstreamlivebs3.m3u8",
            "beIN SPORTS Network",
            false,
            "beIN Sports tertiary 1080p live feed",
            "🏆"
        ),
        Channel(
            "bein_sports_1_tr",
            "beIN Sports 1 Turkey",
            "https://andro.226503.xyz/checklist/androstreamlivebiraz1.m3u8",
            "beIN SPORTS Network",
            false,
            "Turkish region premium soccer and sports",
            "🇹🇷"
        ),

        // Category 3: ESPN Network
        Channel(
            "espn_co",
            "ESPN Colombia",
            "http://181.205.130.194:4000/play/a08n",
            "ESPN Network",
            false,
            "ESPN Colombian sports and tournament Coverage",
            "🇨🇴"
        ),
        Channel(
            "espn_arg",
            "ESPN Argentina",
            "http://45.170.130.224:8000/play/a04l/index.m3u8",
            "ESPN Network",
            false,
            "ESPN Argentinian primary stream",
            "🇦🇷"
        ),
        Channel(
            "espn_premium",
            "ESPN Premium",
            "http://205.235.6.29:8000/play/a0pz/index.m3u8",
            "ESPN Network",
            false,
            "Exclusive premium sport events",
            "🎟️"
        ),
        Channel(
            "espn_main",
            "ESPN Main",
            "http://205.235.6.29:8000/play/a0zf/index.m3u8",
            "ESPN Network",
            false,
            "Main ESPN sports feed",
            "⚡"
        ),
        Channel(
            "espn_7",
            "ESPN 7 Premium",
            "http://205.235.6.29:8000/play/a0zv/index.m3u8",
            "ESPN Network",
            false,
            "ESPN sub-channel with dedicated sports schedule",
            "🏅"
        ),
        Channel(
            "espn_5",
            "ESPN 5",
            "http://181.118.158.103:8000/play/a03c/index.m3u8",
            "ESPN Network",
            false,
            "ESPN 5 live Sports streaming option",
            "⭐"
        ),
        Channel(
            "arg_espn_1",
            "ARG ESPN 1 HD",
            "http://rlatinop.com:8880/josesosa547tv/FFy5GgKfmFgh/327914",
            "ESPN Network",
            false,
            "Latin American feed ESPN 1 in high definition",
            "🥇"
        ),
        Channel(
            "arg_espn_2",
            "ARG ESPN 2 HD",
            "http://rlatinop.com:8880/josesosa547tv/FFy5GgKfmFgh/327915",
            "ESPN Network",
            false,
            "Latin American feed ESPN 2 in high definition",
            "🥈"
        ),
        Channel(
            "arg_espn_3",
            "ARG ESPN 3 HD",
            "http://rlatinop.com:8880/josesosa547tv/FFy5GgKfmFgh/327916",
            "ESPN Network",
            false,
            "Latin American feed ESPN 3 in high definition",
            "🥉"
        ),
        Channel(
            "arg_espn_premium",
            "ARG ESPN Premium",
            "http://rlatinop.com:8880/josesosa547tv/FFy5GgKfmFgh/327917",
            "ESPN Network",
            false,
            "Argentinian professional soccer and league feed",
            "🌟"
        ),
        Channel(
            "espn_premium_hd_co",
            "ESPN Premium HD CO",
            "http://181.205.130.194:4000/play/a0fk",
            "ESPN Network",
            false,
            "Premium quality coverage for major sports",
            "💫"
        ),

        // Category 4: Direct TV Sports (DSports Network)
        Channel(
            "dsports_fhd",
            "DSports FHD (DASH)",
            "https://otte.live.fly.ww.aiv-cdn.net/gru-nitro/live/clients/dash/enc/ubehitlwzo/out/v1/8e09c381a51f4366a19e979418112e8f/cenc.mpd",
            "DSports Network",
            true,
            "Direct TV Sports flagship Full HD Dash stream",
            "💠"
        ),
        Channel(
            "dsports_main",
            "DSports Main (720p)",
            "http://147.135.114.221:8001/play/a00m/index.m3u8",
            "DSports Network",
            false,
            "Direct TV Sports main sports broadcast channel",
            "📶"
        ),
        Channel(
            "dsports_1_arg",
            "DSports 1 Argentina",
            "http://190.117.20.37:8000/play/a08d/index.m3u8",
            "DSports Network",
            false,
            "Dedicated Argentinian tournament broadcast",
            "🇦🇷"
        ),
        Channel(
            "dsports_plus",
            "DSports +",
            "http://217.26.190.76:8888/play/a0jb/index.m3u8",
            "DSports Network",
            false,
            "DSports secondary enhanced channels",
            "➕"
        ),
        Channel(
            "ds_op1",
            "DSports Match Option 1",
            "http://190.108.83.69:8000/play/a05w/index.m3u8",
            "DSports Network",
            false,
            "Interactive match option feeds (Main Stream 1)",
            "1️⃣"
        ),
        Channel(
            "ds_op2",
            "DSports Match Option 2",
            "http://148.222.230.201:8000/play/a0pk/index.m3u8",
            "DSports Network",
            false,
            "Interactive match option feeds (Main Stream 2)",
            "2️⃣"
        ),
        Channel(
            "ds_op3",
            "DSports Match Option 3",
            "http://148.222.230.197:8000/play/a0mm/index.m3u8",
            "DSports Network",
            false,
            "Interactive match option feeds (Main Stream 3)",
            "3️⃣"
        ),
        Channel(
            "ds_op4",
            "DSports Match Option 4",
            "http://181.64.27.65:8000/play/a0dq/index.m3u8",
            "DSports Network",
            false,
            "Interactive match option feeds (Main Stream 4)",
            "4️⃣"
        ),
        Channel(
            "ds_op5",
            "DSports Match Option 5",
            "http://38.187.7.252:8000/play/a03d/index.m3u8",
            "DSports Network",
            false,
            "Interactive match option feeds (Main Stream 5)",
            "5️⃣"
        ),

        // Category 5: DAZN & TUDN Sports
        Channel(
            "dazn_combat",
            "DAZN Combat",
            "https://dazn-combat-rakuten.amagi.tv/hls/amagi_hls_data_rakutenAA-dazn-combat-rakuten/CDN/master.m3u8",
            "DAZN & TUDN Sports",
            false,
            "DAZN 24/7 Mixed martial arts, wrestling and fight sports",
            "🥊"
        ),
        Channel(
            "dazn_fast",
            "DAZN Fast+",
            "https://ac-003.live.p7s1video.net/63f8995e/t_003/dazn-fast-hd/cenc-default.mpd",
            "DAZN & TUDN Sports",
            true,
            "DAZN Fast free streaming sports feed",
            "⚡"
        ),
        Channel(
            "dazn_2",
            "DAZN 2",
            "http://23.237.104.106:8080/DAZN_2/index.m3u8",
            "DAZN & TUDN Sports",
            false,
            "Primary football coverage in Europe",
            "🔥"
        ),
        Channel(
            "dazn_3",
            "DAZN 3",
            "http://23.237.104.106:8080/DAZN_3/index.m3u8",
            "DAZN & TUDN Sports",
            false,
            "European league streaming backup 2",
            "🛡️"
        ),
        Channel(
            "tudn_hd",
            "TUDN HD Play",
            "http://24.152.53.3:8090/play/a012/index.m3u8",
            "DAZN & TUDN Sports",
            false,
            "Televisa Univision Deportes Network flagship stream",
            "🇲🇽"
        ),
        Channel(
            "tudn_usa",
            "TUDN USA",
            "http://m3u.tvcluboficial.com/m/m/957.m3u8",
            "DAZN & TUDN Sports",
            false,
            "Televisa Univision Deportes Network USA",
            "🇺🇸"
        ),

        // Category 6: Sky & Fox Sports
        Channel(
            "sky_sports_action",
            "Sky Sports Action",
            "http://sewv654wfcsdwfi87fwvgbngh.siauliairsavlt.pw/iptv/VCQ4ADX96VH4G8PY7URBWRQU/9155/index.m3u8",
            "Sky & Fox Sports",
            false,
            "Action-packed sports schedule by Sky UK",
            "💥"
        ),
        Channel(
            "sky_sport_laliga",
            "Sky Sport LaLiga MX",
            "http://179.60.224.196:8000/play/a0i7/index.m3u8",
            "Sky & Fox Sports",
            false,
            "Sky sports exclusive Spanish LaLiga coverage (Mexico)",
            "🇲🇽"
        ),
        Channel(
            "fox_sports_720",
            "FOX Sports (720p)",
            "https://d1jzu95oc8fgt3.cloudfront.net/FOX_Sports720p.m3u8",
            "Sky & Fox Sports",
            false,
            "Fox Sports standard 720p streaming feed",
            "📺"
        ),
        Channel(
            "fox_sports1",
            "FOX Sports 1 (FS1)",
            "http://41.223.30.230/FOXSPORTS1/index.m3u8",
            "Sky & Fox Sports",
            false,
            "FS1 US sporting events broadcasts",
            "📰"
        ),
        Channel(
            "fox_deportes",
            "FOX Deportes",
            "http://23.237.104.106:8080/USA_FOX_DEPORTES/index.m3u8",
            "Sky & Fox Sports",
            false,
            "Fox Sports Spanish-language American coverage",
            "🇪🇸"
        ),
        Channel(
            "fox_11_green_bay",
            "FOX 11 Green Bay",
            "https://linear-702.frequency.stream/dist/stirr/702/hls/master/playlist.m3u8",
            "Sky & Fox Sports",
            false,
            "FOX 11 regional affiliate sports broadcasts",
            "🏈"
        ),

        // Category 7: Adrenaline & Action (Motorsports)
        Channel(
            "redbull_tv",
            "Red Bull TV (Global)",
            "https://rbmn-live.akamaized.net/hls/live/590964/BoRB-AT/master_3360.m3u8",
            "Adrenaline & Action",
            false,
            "Red Bull live action, engineering, and motorsports",
            "🏍️"
        ),
        Channel(
            "redbull_br",
            "Red Bull TV Brasil",
            "https://d03ae6b5c6724c24867e97a3dc04934a.mediatailor.us-west-2.amazonaws.com/v1/master/ba62fe743df0fe93366eba3a257d792884136c7f/LINEAR-1026-WORBBRPTFAST-WHALETVPLUS/1026/hls/master/playlist.m3u8",
            "Adrenaline & Action",
            false,
            "Red Bull live action stream in Portuguese",
            "🇧🇷"
        ),
        Channel(
            "gopro_tv",
            "GoPro TV Channel",
            "https://3a1b4d927c02473b806350cc162d271f.mediatailor.us-west-2.amazonaws.com/v1/master/ba62fe743df0fe93366eba3a257d792884136c7f/LINEAR-891-GOPRO-FREELIVESPORTS/mt/freelivesports/891/hls/master/playlist.m3u8",
            "Adrenaline & Action",
            false,
            "Awesome high-octane user contents and POV clips",
            "📹"
        ),
        Channel(
            "fight_network",
            "Fight Network",
            "https://amg00966-amg00966c10-amgplt0201.playout.now3.amagi.tv/playlist/amg00966-amg00966c10-amgplt0201/playlist.m3u8",
            "Adrenaline & Action",
            false,
            "Mixed martial arts, boxing and professional wrestling",
            "⚔️"
        ),
        Channel(
            "fuel_tv",
            "FUEL TV Action",
            "https://cdn-uw2-prod.tsv2.amagi.tv/linear/amg01074-fueltv-fueltvemeaen-sportstribal/playlist.m3u8",
            "Adrenaline & Action",
            false,
            "Global skateboarding, snowboarding, and surfing channel",
            "🏄"
        ),
        Channel(
            "drone_tv",
            "Drone TV Network",
            "https://a480c1a918694b47b63f7ba07a0f1dc2.mediatailor.us-east-1.amazonaws.com/v1/master/44f73ba4d03e9607dcd9bebdcb8494d86964f1d8/SportsTribal-eu_DroneTV/playlist.m3u8",
            "Adrenaline & Action",
            false,
            "Live FPV drone racing flights and landscapes",
            "🛸"
        ),

        // Category 8: South Asian Sports
        Channel(
            "star_sports_1",
            "Star Sports 1 HD",
            "http://41.205.93.154/STARSPORTS1/index.m3u8",
            "South Asian Sports",
            false,
            "Indian subcontinental legacy cricket and soccer",
            "🏏"
        ),
        Channel(
            "sony_liv_155",
            "Sony LIV Stream 1",
            "http://live.balajibroadband.com:3500/live/155.m3u8",
            "South Asian Sports",
            false,
            "Balaji Broadband Sony LIV Sports source 1",
            "📺"
        ),
        Channel(
            "sony_liv_162",
            "Sony LIV Stream 2",
            "http://live.balajibroadband.com:3500/live/162.m3u8",
            "South Asian Sports",
            false,
            "Balaji Broadband Sony LIV Sports source 2",
            "💻"
        ),
        Channel(
            "sony_liv_524",
            "Sony LIV Stream 3",
            "http://live.balajibroadband.com:3500/live/524.m3u8",
            "South Asian Sports",
            false,
            "Balaji Broadband Sony LIV Sports source 3",
            "📡"
        ),
        Channel(
            "t_sports_ts",
            "T Sports Bangladesh",
            "http://luckonline.eu/live/y49sz6KMQs/6115263489/1142.ts",
            "South Asian Sports",
            false,
            "Sports television channel focused on live soccer/cricket",
            "🔴"
        ),
        Channel(
            "fancode_dash",
            "FANCODE Premium (DASH)",
            "https://otte.live.fly.ww.aiv-cdn.net/sin-nitro/live/clients/dash-sd/enc/v8g0dlo4z8/out/v1/946019f85dc64ae99ff9ce64a9727a62/cenc-sd.mpd",
            "South Asian Sports",
            true,
            "Fancode live sports dash streaming coverage",
            "🌟"
        ),

        // Category 9: Local & General
        Channel(
            "btv_live",
            "বিটিভি (BTV Live)",
            "https://owrcovcrpy.gpcdn.net/bpk-tv/1709/output/index.m3u8",
            "Local & General",
            false,
            "Bangladesh Television national public broadcaster",
            "🇧🇩"
        ),
        Channel(
            "mytv_live",
            "মাই টিভি (My TV)",
            "http://116.204.149.16/mytv/index.m3u8",
            "Local & General",
            false,
            "Bangladeshi Bengali-language satellite TV channel",
            "📺"
        ),
        Channel(
            "rajdhani_tv",
            "RAJDHANI TV",
            "https://sm-monirul.top/toffee/play/rajdhani_tv.m3u8",
            "Local & General",
            false,
            "Bangladeshi regional and entertainment channel",
            "🗼"
        ),
        Channel(
            "real_madrid_tv_es",
            "Real Madrid TV (ES)",
            "https://rmtv.akamaized.net/hls/live/2043153/rmtv-es-web/bitrate_3.m3u8",
            "Local & General",
            false,
            "Real Madrid proprietary club channel in Spanish",
            "🇪🇸"
        ),
        Channel(
            "real_madrid_tv_web",
            "Real Madrid TV (Web)",
            "https://rmtv.akamaized.net/hls/live/2043153/rmtv-es-web/master.m3u8",
            "Local & General",
            false,
            "Real Madrid web streaming feed",
            "👑"
        )
    )
}
