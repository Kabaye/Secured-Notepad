package edu.bsu.sn.client.notepad.controller;

import edu.bsu.sn.client.notepad.model.FileContent;
import edu.bsu.sn.client.notepad.model.UserFiles;
import edu.bsu.sn.client.notepad.service.NotepadService;
import edu.bsu.sn.client.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.JOptionPane;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotepadController {
    private final NotepadService notepadService;
    private final SecurityService securityService;

    @GetMapping
    public FileContent getFileContent(@RequestParam("file-name") String fileName, @RequestParam("username") String username) {
        return notepadService.getFileContent(fileName, username);
    }

    @GetMapping("/test")
    public FileContent getFContent() {
        securityService.logIn("Kabaye");
//        return notepadService.getFileContent("README.txt", "Kabaye");
        final FileContent fileContent = notepadService.getFileContent("Родина - Михаил Лермонтов.txt", "Kabaye");
        JOptionPane.showMessageDialog(null, fileContent.getFileContent());
        return fileContent;
    }

    @GetMapping("/test1")
    public UserFiles getUserFiles() {
        securityService.logIn("Kabaye");
        return notepadService.getUserFiles("Kabaye");
    }

    @GetMapping("/test2")
    public boolean deleteFile() {
        securityService.logIn("Kabaye");
        return notepadService.deleteUserFile("Родина - Михаил Лермонтов.txt", "Kabaye");
    }

    @GetMapping("/test3")
    public FileContent updateFile() {
        securityService.logIn("Kabaye");
        return notepadService.updateUserFile(new FileContent()
                .setFileName("Родина - Михаил Лермонтов.txt")
                .setUsername("Kabaye")
                .setFileContent("Когда она приезжала из частной школы домой на каникулы, я мог видеть ее чуть не каждый день: дом их стоял через дорогу, прямо против того крыла Ратуши, где я работал. Она то и дело мчалась куда-то, одна или вместе с сестренкой, а то и с какими-нибудь молодыми людьми. Вот это мне было вовсе не по вкусу. Иногда выдавалась минутка, я отрывался от своих гроссбухов и папок, подходил к окну и смотрел туда, на их дом, поверх матовых стекол, ну, бывало, и увижу ее. А вечером занесу это в дневник наблюдений. Сперва обозначал ее индексом «Х», а после, когда узнал, как ее звать, «M». Несколько раз встречал на улице, а как-то стоял прямо за ней в очереди в библиотеке на Кроссфилд-стрит. Она и не обернулась ни разу, а я долго смотрел на ее затылок, на волосы, заплетенные в длинную косу, очень светлые, шелковистые, словно кокон тутового шелкопряда. И собраны в одну косу, длинную, до пояса. То она ее на грудь перекидывала, то снова на спину. А то вокруг головы укладывала. И пока она не стала гостьей здесь, в моем доме, мне только раз посчастливилось увидеть эти волосы свободно рассыпавшимися по плечам. У меня прямо горло перехватило, так это было красиво. Ну точно русалка.\n" +
                        "А в другой раз, в субботу, я поехал в Музей естественной истории, в Лондон, и мы возвращались в одном вагоне. Она сидела на третьей от меня скамейке, ко мне боком, и читала, а я целых полчаса на нее смотрел. Смотреть на нее было для меня ну все равно как за бабочкой охотиться, как редкий экземпляр ловить. Крадешься осторожненько, душа в пятки ушла, как говорится… Будто перламутровку ловишь. Я хочу сказать, я о ней думал всегда такими словами, как «неуловимая», «ускользающая», «редкостная»… В ней была какая-то утонченность, не то что в других, даже очень хорошеньких. Она была — для знатока. Для тех, кто понимает.")
        );
    }
}
