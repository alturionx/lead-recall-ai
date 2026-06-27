
import QRCode from "qrcode"
import { makeWASocket, useMultiFileAuthState, fetchLatestWaWebVersion, DisconnectReason } from "baileys";

async function loadESMModule() {
    try {
        const { state, saveCreds } = await useMultiFileAuthState("auth_info_baileys");
        const { version } = await fetchLatestWaWebVersion();

        const socket = makeWASocket(
            {
                auth: state,
                version
            }
        );

        socket.ev.on('connection.update', async (update) => {
            const { connection, lastDisconnect, qr } = update;
            if (qr) {
                console.log(await QRCode.toString(qr, { type: 'terminal', small: true }))
            }
            if (connection === 'close' && (lastDisconnect?.error)?.output?.statusCode === DisconnectReason.restartRequired) {
                loadESMModule();
            }
        })

        socket.ev.on('messages.upsert', ({ type, messages }) => {
            if (type == "notify") { // new messages
                for (const message of messages) {

                    // Ignora eventos sem mensagem
                    if (!message.message) continue;

                    const msg = message.message.conversation;
                    const fromMe = message.key.fromMe;
                    const remoteJid = message.key.remoteJid;
                    const remoteJidAlt = message.key.remoteJidAlt;
                    const pushName = message.pushName;

                    // Ignora mensagens enviadas por você
                    if (fromMe) continue;

                    // Ignora grupos
                    if (remoteJid?.endsWith("@g.us")) continue;

                    const payload = {
                        msg,
                        remoteJid,
                        remoteJidAlt,
                        pushName
                    };

                    console.log(payload);
                }
            } else {
            }
        })

        socket.ev.on("creds.update", saveCreds);

        console.log("Socket criado com sucesso!");

    } catch (error) {

        console.error("Erro ao criar o socket:", error);

    }
}

loadESMModule();