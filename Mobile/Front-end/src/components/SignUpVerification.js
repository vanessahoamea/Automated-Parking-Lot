import React from "react";
import { StyleSheet, View } from "react-native";
import { StatusBar } from "expo-status-bar";
import MainButton from "./MainButton";
import {
  DefaultTheme,
  Headline,
  Provider as PaperProvider,
  TextInput,
  Text,
} from "react-native-paper";
import { useRoute } from "@react-navigation/native";

const theme = {
  ...DefaultTheme,
  roundness: 2,
  colors: {
    ...DefaultTheme.colors,
    primary: "#004643",
    accent: "#E16162",
    background: "#004643",
    surface: "#FCFCFF",
    text: "#FFFFFE",
    placeholder: "#ABD1C6",
  },
};

export default function SignUpPhone({ navigation }) {
  const route = useRoute();

  return (
    <PaperProvider theme={theme}>
      <View style={styles.container}>
        <Headline style={styles.logo}>LOGO</Headline>
        <TextInput
          style={styles.input}
          placeholder="verification code"
        ></TextInput>
        <View>
          <Text style={styles.text}>
            Didn't receive code? <Text style={styles.linkedText}>Resend</Text>
          </Text>
        </View>
        <MainButton
          text="Next"
          onPress={() =>
            navigation.navigate("SignUpPassword", {
              email: route.params.email,
              phoneNumber: route.params.phoneNumber,
            })
          }
        />
        <StatusBar style="auto" />
      </View>
    </PaperProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#004643",
    alignItems: "center",
    justifyContent: "center",
  },
  logo: {
    color: "#E16162",
    paddingBottom: 80,
  },
  input: {
    width: "90%",
    height: 44,
    borderBottomWidth: 1,
    borderBottomColor: "#A6AAB4",
    margin: 10,
    marginBottom: 20,
    marginTop: 20,
  },
  text: {
    marginBottom: 70,
  },
  linkedText: {
    color: "#F9BC60",
    textAlign: "right",
    marginBottom: 50,
    fontWeight: "700",
  },
});
