import React, { useState } from 'react';

import { StyleSheet, Button, View } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import MainButton from './mainButton';

import {
	DefaultTheme,
	Headline,
	Provider as PaperProvider,
	TextInput,
	Text,
} from 'react-native-paper';

const theme = {
	...DefaultTheme,
	roundness: 2,
	colors: {
		...DefaultTheme.colors,
		primary: '#004643',
		accent: '#E16162',
		background: '#004643',
		surface: '#FCFCFF',
		text: '#FFFFFE',
		placeholder: '#ABD1C6',
	},
};

export default function SignUp({ navigation }) {
	// state = {
    //     email: 'demo',
    // };
	return (
		<PaperProvider theme={theme}>
		<View style={styles.container}>
			<Headline style={styles.logo}>LOGO</Headline>
				<Text>Enter your email address</Text>
					<TextInput
						style={styles.input}
						placeholder='email adress'
						// onChangeText={(value) => this.setState({email: value})}
						// value={this.state.email}
					></TextInput>
			<MainButton text='Next' title='Sign Up' onPress={() =>navigation.navigate('SignUpPhone')}/>
			<StatusBar style='auto' />
		</View>
	</PaperProvider>
	);
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		backgroundColor: '#004643',
		alignItems: 'center',
		justifyContent: 'center',
	},
	logo: {
		color: '#E16162',
		paddingBottom: 80
	},
	input: {
		width: '90%',
		height: 44,
		borderBottomWidth: 1,
		borderBottomColor: '#A6AAB4',
		margin: 10,
		marginBottom: 80,
		marginTop: 20
	},
	linkedText: {
		color: '#F9BC60',
		textAlign: 'right',
		marginBottom: 50,
		fontWeight: '700',
	}
});